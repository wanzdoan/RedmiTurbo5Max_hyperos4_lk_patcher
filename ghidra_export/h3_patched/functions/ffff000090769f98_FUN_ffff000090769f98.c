// ffff000090769f98  size=540  FUN_ffff000090769f98

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090769f98(void)

{
  if (DAT_ffff000090831e28 == 0xfffb) {
    (*_DAT_ffff00009087cd88)(DAT_ffff000090831e2c);
  }
  else if (DAT_ffff000090831e28 != 0xfffd) {
    if (DAT_ffff000090831e28 == 0xfffc) {
      (*_DAT_ffff00009087cd90)(DAT_ffff000090831e2c);
    }
    else {
      (*_DAT_ffff00009087cdc0)(DAT_ffff000090831e28,DAT_ffff000090831e2c,&DAT_ffff000090831e2d,1);
    }
  }
  if (DAT_ffff000090831ef8 != 0xfffd) {
    if (DAT_ffff000090831ef8 == 0xfffc) {
      (*_DAT_ffff00009087cd90)(DAT_ffff000090831efc);
    }
    else if (DAT_ffff000090831ef8 == 0xfffb) {
      (*_DAT_ffff00009087cd88)(DAT_ffff000090831efc);
    }
    else {
      (*_DAT_ffff00009087cdc0)(DAT_ffff000090831ef8,DAT_ffff000090831efc,&DAT_ffff000090831efd,1);
    }
  }
  if (DAT_ffff000090831fc8 != 0xfffd) {
    if (DAT_ffff000090831fc8 == 0xfffc) {
      (*_DAT_ffff00009087cd90)(DAT_ffff000090831fcc);
    }
    else if (DAT_ffff000090831fc8 == 0xfffb) {
      (*_DAT_ffff00009087cd88)(DAT_ffff000090831fcc);
    }
    else {
      (*_DAT_ffff00009087cdc0)(DAT_ffff000090831fc8,DAT_ffff000090831fcc,&DAT_ffff000090831fcd,1);
    }
  }
  if (DAT_ffff000090832098 != 0xfffd) {
    if (DAT_ffff000090832098 == 0xfffc) {
      (*_DAT_ffff00009087cd90)(DAT_ffff00009083209c);
    }
    else if (DAT_ffff000090832098 == 0xfffb) {
      (*_DAT_ffff00009087cd88)(DAT_ffff00009083209c);
    }
    else {
      (*_DAT_ffff00009087cdc0)(DAT_ffff000090832098,DAT_ffff00009083209c,&DAT_ffff00009083209d,1);
    }
  }
  FUN_ffff000090785468(0x5a,0);
  FUN_ffff000090785354(0x5a,1);
  FUN_ffff0000907853c4(0x5a,0);
                    /* WARNING: Could not recover jumptable at 0xffff00009076a1b0. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*_DAT_ffff00009087cd90)(10);
  return;
}


