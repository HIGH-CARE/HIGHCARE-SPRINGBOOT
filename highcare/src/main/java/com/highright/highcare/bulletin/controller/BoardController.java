package com.highright.highcare.bulletin.controller;

import com.highright.highcare.bulletin.dto.BoardDTO;
import com.highright.highcare.bulletin.dto.CommentDTO;
import com.highright.highcare.bulletin.dto.BoardPagingResponseDTO;
import com.highright.highcare.bulletin.dto.BulletinCategoriesDTO;
import com.highright.highcare.bulletin.sevice.BoardService;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.common.PageDTO;
import com.highright.highcare.common.PagingResponseDTO;
import com.highright.highcare.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bulletin")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    @Operation(summary = "게시판 글목록 조회", description = "게시판에 글목록을 조회합니다", tags = {"BoardController"})
    @GetMapping("/board")
    public ResponseEntity<ResponseDTO> selectBoardList(
            @RequestParam(name = "categoryCode") String categoryCode,
            @RequestParam(name = "currentPage") String currentPage,
            @RequestParam(name = "content") String content,
            @RequestParam(name = "empNo")int empNo) {

        int boardCategoryCode = Integer.valueOf(categoryCode);
        Criteria cri = new Criteria(Integer.valueOf(currentPage), 10);
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        int total = 0;
        if(content == ""){
             total = boardService.selectBoardTotal(boardCategoryCode,empNo);

            pagingResponseDTO.setData(boardService.selectBoardListWithPaging(cri, boardCategoryCode,empNo));

        }else{
             total = boardService.selectSearchTotal(boardCategoryCode, content,empNo);
            pagingResponseDTO.setData(boardService.selectBoardListWithPagingSearch(cri, boardCategoryCode, content,empNo));
        }
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "조회 성공", pagingResponseDTO));
    }
    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다", tags = {"BoardController"})
    @GetMapping("/thread")
    public ResponseEntity<ResponseDTO> selectBoardDetail(
            @RequestParam(name = "bulletinCode") String bulletinCode
    ) {
        int code = Integer.parseInt(bulletinCode);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "글 조회 성공", boardService.selectBoard(code)));
    }
    @Operation(summary = "게시글 댓글 조회", description = "게시글 댓글을 조회합니다", tags = {"BoardController"})
    @GetMapping("/comment")
    public ResponseEntity<ResponseDTO> selectCommentDetail(
            @RequestParam(name = "bulletinCode") String bulletinCode,
            @RequestParam(name = "currentPage") String currentPage
    ) {
        int total = boardService.selectCommentTotal(bulletinCode);
        int code = Integer.parseInt(bulletinCode);

        Criteria cri = new Criteria(Integer.valueOf(currentPage), 5);
        BoardPagingResponseDTO pagingResponseDTO = new BoardPagingResponseDTO();
        pagingResponseDTO.setData(boardService.selectBoardAndCommentPaging(cri, bulletinCode));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));
        pagingResponseDTO.setTotal(total);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "댓글 조회 성공", pagingResponseDTO));
    }

    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다", tags = {"BoardController"})
    @GetMapping("/thr")
    public ResponseEntity<ResponseDTO> selectBoardDetails(
            @RequestParam(name = "bulletinCode") String bulletinCode
    ){
        int code = Integer.parseInt(bulletinCode);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "글 조회 성공", boardService.selectBoards(code)));
    }
    @Operation(summary = "게시판 카테고리 조회", description = "게시판 카테고리를 조회합니다", tags = {"BoardController"})

    @GetMapping("/boardTitle")
    public ResponseEntity<ResponseDTO> selectBoardTitle() {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "게시글 조회 성공", boardService.selectBoardTitle()));

    }
    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다", tags = {"BoardController"})
    @PostMapping("/insertBoard")
    public ResponseEntity<ResponseDTO> insertBoard(@RequestBody BoardDTO boardDTO) {
        System.out.println("boardDTO get empNo : " + boardDTO.getEmpNo());
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED.value(), "글쓰기 성공", boardService.insertBoard(boardDTO)));
    }
    @Operation(summary = "댓글 작성", description = "게시글 댓글을 작성합니다", tags = {"BoardController"})
    @PostMapping("/insertComment")
    public ResponseEntity<ResponseDTO> insertComment(@RequestBody BoardDTO boardDTO) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED.value(), "댓글쓰기 성공", boardService.insertComment(boardDTO)));
    }
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다", tags = {"BoardController"})
    @PutMapping("/updateBoard")
    public ResponseEntity<ResponseDTO> updateBoard(@RequestBody BoardDTO boardDTO){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED.value(), "글 수정 성공", boardService.updateBoard(boardDTO)));
    }
    @Operation(summary = "게시글 수정", description = "게시글의 상태를 수정합니다", tags = {"BoardController"})
    @PutMapping("/deleteBoard")
    public ResponseEntity<ResponseDTO> deleteBoard(@RequestBody BoardDTO boardDTO){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED.value(), "글 삭제 성공", boardService.deleteBoard(boardDTO)));
    }
    @Operation(summary = "댓글 수정", description = "댓글의 상태를 수정합니다", tags = {"BoardController"})
    @PutMapping("/deleteComment")
    public ResponseEntity<ResponseDTO> deleteBoard(@RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED.value(), "댓글 삭제 성공", boardService.deleteComment(commentDTO)));

    }
    @Operation(summary = "게시글 댓글 수정", description = "게시글 댓글을 수정합니다", tags = {"BoardController"})
    @PutMapping("/updateComment")
    public ResponseEntity<ResponseDTO> updateBoard(@RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED.value(), "댓글 수정 성공", boardService.updateComment(commentDTO)));

    }
    @Operation(summary = "공지 조회", description = "공지를 조회합니다", tags = {"BoardController"})
    @GetMapping("/notice")
    public ResponseEntity<ResponseDTO> selectnotice(){
        System.out.println("notice noticenoticenoticenotice = " + boardService);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "공지 조회 성공", boardService.selectNotice()));

    }
}
