
void FUN_ffff0000be052534(void)

{
  if (DAT_ffff0000be0ee840 == 0xfffb) {
    (*DAT_ffff0000be127040)(DAT_ffff0000be0ee844);
  }
  else if (DAT_ffff0000be0ee840 != 0xfffd) {
    if (DAT_ffff0000be0ee840 == 0xfffc) {
      (*DAT_ffff0000be127048)(DAT_ffff0000be0ee844);
    }
    else {
      (*DAT_ffff0000be127078)(DAT_ffff0000be0ee840,DAT_ffff0000be0ee844,&DAT_ffff0000be0ee845,1);
    }
  }
  if (DAT_ffff0000be0ee910 != 0xfffd) {
    if (DAT_ffff0000be0ee910 == 0xfffc) {
      (*DAT_ffff0000be127048)(DAT_ffff0000be0ee914);
    }
    else if (DAT_ffff0000be0ee910 == 0xfffb) {
      (*DAT_ffff0000be127040)(DAT_ffff0000be0ee914);
    }
    else {
      (*DAT_ffff0000be127078)(DAT_ffff0000be0ee910,DAT_ffff0000be0ee914,&DAT_ffff0000be0ee915,1);
    }
  }
  if (DAT_ffff0000be0ee9e0 != 0xfffd) {
    if (DAT_ffff0000be0ee9e0 == 0xfffc) {
      (*DAT_ffff0000be127048)(DAT_ffff0000be0ee9e4);
    }
    else if (DAT_ffff0000be0ee9e0 == 0xfffb) {
      (*DAT_ffff0000be127040)(DAT_ffff0000be0ee9e4);
    }
    else {
      (*DAT_ffff0000be127078)(DAT_ffff0000be0ee9e0,DAT_ffff0000be0ee9e4,&DAT_ffff0000be0ee9e5,1);
    }
  }
  if (DAT_ffff0000be0eeab0 != 0xfffd) {
    if (DAT_ffff0000be0eeab0 == 0xfffc) {
      (*DAT_ffff0000be127048)(DAT_ffff0000be0eeab4);
    }
    else if (DAT_ffff0000be0eeab0 == 0xfffb) {
      (*DAT_ffff0000be127040)(DAT_ffff0000be0eeab4);
    }
    else {
      (*DAT_ffff0000be127078)(DAT_ffff0000be0eeab0,DAT_ffff0000be0eeab4,&DAT_ffff0000be0eeab5,1);
    }
  }
  FUN_ffff0000be065ed8(0x5a,0);
  FUN_ffff0000be065dc4(0x5a,1);
  FUN_ffff0000be065e34(0x5a,0);
                    /* WARNING: Could not recover jumptable at 0xffff0000be05274c. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*DAT_ffff0000be127048)(10);
  return;
}


