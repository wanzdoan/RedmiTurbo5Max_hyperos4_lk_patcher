
void FUN_ffff0000b80447cc(void)

{
  if (DAT_ffff0000b8101770 == 0xfffb) {
    (*DAT_ffff0000b8129af8)(DAT_ffff0000b8101774);
  }
  else if (DAT_ffff0000b8101770 != 0xfffd) {
    if (DAT_ffff0000b8101770 == 0xfffc) {
      (*DAT_ffff0000b8129b00)(DAT_ffff0000b8101774);
    }
    else {
      (*DAT_ffff0000b8129b30)(DAT_ffff0000b8101770,DAT_ffff0000b8101774,&DAT_ffff0000b8101775,1);
    }
  }
  if (DAT_ffff0000b8101840 != 0xfffd) {
    if (DAT_ffff0000b8101840 == 0xfffc) {
      (*DAT_ffff0000b8129b00)(DAT_ffff0000b8101844);
    }
    else if (DAT_ffff0000b8101840 == 0xfffb) {
      (*DAT_ffff0000b8129af8)(DAT_ffff0000b8101844);
    }
    else {
      (*DAT_ffff0000b8129b30)(DAT_ffff0000b8101840,DAT_ffff0000b8101844,&DAT_ffff0000b8101845,1);
    }
  }
  if (DAT_ffff0000b8101910 != 0xfffd) {
    if (DAT_ffff0000b8101910 == 0xfffc) {
      (*DAT_ffff0000b8129b00)(DAT_ffff0000b8101914);
    }
    else if (DAT_ffff0000b8101910 == 0xfffb) {
      (*DAT_ffff0000b8129af8)(DAT_ffff0000b8101914);
    }
    else {
      (*DAT_ffff0000b8129b30)(DAT_ffff0000b8101910,DAT_ffff0000b8101914,&DAT_ffff0000b8101915,1);
    }
  }
  if (DAT_ffff0000b81019e0 != 0xfffd) {
    if (DAT_ffff0000b81019e0 == 0xfffc) {
      (*DAT_ffff0000b8129b00)(DAT_ffff0000b81019e4);
    }
    else if (DAT_ffff0000b81019e0 == 0xfffb) {
      (*DAT_ffff0000b8129af8)(DAT_ffff0000b81019e4);
    }
    else {
      (*DAT_ffff0000b8129b30)(DAT_ffff0000b81019e0,DAT_ffff0000b81019e4,&DAT_ffff0000b81019e5,1);
    }
  }
  FUN_ffff0000b805098c(0x5a,0);
  FUN_ffff0000b8050878(0x5a,1);
  FUN_ffff0000b80508e8(0x5a,0);
                    /* WARNING: Could not recover jumptable at 0xffff0000b80449e4. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*DAT_ffff0000b8129b00)(10);
  return;
}


