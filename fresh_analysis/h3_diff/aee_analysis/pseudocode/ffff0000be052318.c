
void FUN_ffff0000be052318(void)

{
  if (DAT_ffff0000be0ee500 == 0xfffb) {
    (*DAT_ffff0000be126f48)(DAT_ffff0000be0ee504);
  }
  else if (DAT_ffff0000be0ee500 != 0xfffd) {
    if (DAT_ffff0000be0ee500 == 0xfffc) {
      (*DAT_ffff0000be126f50)(DAT_ffff0000be0ee504);
    }
    else {
      (*DAT_ffff0000be126f80)(DAT_ffff0000be0ee500,DAT_ffff0000be0ee504,&DAT_ffff0000be0ee505,1);
    }
  }
  if (DAT_ffff0000be0ee5d0 != 0xfffd) {
    if (DAT_ffff0000be0ee5d0 == 0xfffc) {
      (*DAT_ffff0000be126f50)(DAT_ffff0000be0ee5d4);
    }
    else if (DAT_ffff0000be0ee5d0 == 0xfffb) {
      (*DAT_ffff0000be126f48)(DAT_ffff0000be0ee5d4);
    }
    else {
      (*DAT_ffff0000be126f80)(DAT_ffff0000be0ee5d0,DAT_ffff0000be0ee5d4,&DAT_ffff0000be0ee5d5,1);
    }
  }
  if (DAT_ffff0000be0ee6a0 != 0xfffd) {
    if (DAT_ffff0000be0ee6a0 == 0xfffc) {
      (*DAT_ffff0000be126f50)(DAT_ffff0000be0ee6a4);
    }
    else if (DAT_ffff0000be0ee6a0 == 0xfffb) {
      (*DAT_ffff0000be126f48)(DAT_ffff0000be0ee6a4);
    }
    else {
      (*DAT_ffff0000be126f80)(DAT_ffff0000be0ee6a0,DAT_ffff0000be0ee6a4,&DAT_ffff0000be0ee6a5,1);
    }
  }
  if (DAT_ffff0000be0ee770 != 0xfffd) {
    if (DAT_ffff0000be0ee770 == 0xfffc) {
      (*DAT_ffff0000be126f50)(DAT_ffff0000be0ee774);
    }
    else if (DAT_ffff0000be0ee770 == 0xfffb) {
      (*DAT_ffff0000be126f48)(DAT_ffff0000be0ee774);
    }
    else {
      (*DAT_ffff0000be126f80)(DAT_ffff0000be0ee770,DAT_ffff0000be0ee774,&DAT_ffff0000be0ee775,1);
    }
  }
  FUN_ffff0000be065ed8(0x5a,0);
  FUN_ffff0000be065dc4(0x5a,1);
  FUN_ffff0000be065e34(0x5a,0);
                    /* WARNING: Could not recover jumptable at 0xffff0000be052530. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*DAT_ffff0000be126f50)(10);
  return;
}


