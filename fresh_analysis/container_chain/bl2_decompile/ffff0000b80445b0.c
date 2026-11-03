
void FUN_ffff0000b80445b0(void)

{
  if (DAT_ffff0000b8101430 == 0xfffb) {
    (*DAT_ffff0000b8129a00)(DAT_ffff0000b8101434);
  }
  else if (DAT_ffff0000b8101430 != 0xfffd) {
    if (DAT_ffff0000b8101430 == 0xfffc) {
      (*DAT_ffff0000b8129a08)(DAT_ffff0000b8101434);
    }
    else {
      (*DAT_ffff0000b8129a38)(DAT_ffff0000b8101430,DAT_ffff0000b8101434,&DAT_ffff0000b8101435,1);
    }
  }
  if (DAT_ffff0000b8101500 != 0xfffd) {
    if (DAT_ffff0000b8101500 == 0xfffc) {
      (*DAT_ffff0000b8129a08)(DAT_ffff0000b8101504);
    }
    else if (DAT_ffff0000b8101500 == 0xfffb) {
      (*DAT_ffff0000b8129a00)(DAT_ffff0000b8101504);
    }
    else {
      (*DAT_ffff0000b8129a38)(DAT_ffff0000b8101500,DAT_ffff0000b8101504,&DAT_ffff0000b8101505,1);
    }
  }
  if (DAT_ffff0000b81015d0 != 0xfffd) {
    if (DAT_ffff0000b81015d0 == 0xfffc) {
      (*DAT_ffff0000b8129a08)(DAT_ffff0000b81015d4);
    }
    else if (DAT_ffff0000b81015d0 == 0xfffb) {
      (*DAT_ffff0000b8129a00)(DAT_ffff0000b81015d4);
    }
    else {
      (*DAT_ffff0000b8129a38)(DAT_ffff0000b81015d0,DAT_ffff0000b81015d4,&DAT_ffff0000b81015d5,1);
    }
  }
  if (DAT_ffff0000b81016a0 != 0xfffd) {
    if (DAT_ffff0000b81016a0 == 0xfffc) {
      (*DAT_ffff0000b8129a08)(DAT_ffff0000b81016a4);
    }
    else if (DAT_ffff0000b81016a0 == 0xfffb) {
      (*DAT_ffff0000b8129a00)(DAT_ffff0000b81016a4);
    }
    else {
      (*DAT_ffff0000b8129a38)(DAT_ffff0000b81016a0,DAT_ffff0000b81016a4,&DAT_ffff0000b81016a5,1);
    }
  }
  FUN_ffff0000b805098c(0x5a,0);
  FUN_ffff0000b8050878(0x5a,1);
  FUN_ffff0000b80508e8(0x5a,0);
                    /* WARNING: Could not recover jumptable at 0xffff0000b80447c8. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*DAT_ffff0000b8129a08)(10);
  return;
}


