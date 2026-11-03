
void FUN_ffff00009076a260(void)

{
  if (DAT_ffff000090831e48 == 0xfffb) {
    (*DAT_ffff00009087ce20)(DAT_ffff000090831e4c);
  }
  else if (DAT_ffff000090831e48 != 0xfffd) {
    if (DAT_ffff000090831e48 == 0xfffc) {
      (*DAT_ffff00009087ce28)(DAT_ffff000090831e4c);
    }
    else {
      (*DAT_ffff00009087ce58)(DAT_ffff000090831e48,DAT_ffff000090831e4c,&DAT_ffff000090831e4d,1);
    }
  }
  if (DAT_ffff000090831f18 != 0xfffd) {
    if (DAT_ffff000090831f18 == 0xfffc) {
      (*DAT_ffff00009087ce28)(DAT_ffff000090831f1c);
    }
    else if (DAT_ffff000090831f18 == 0xfffb) {
      (*DAT_ffff00009087ce20)(DAT_ffff000090831f1c);
    }
    else {
      (*DAT_ffff00009087ce58)(DAT_ffff000090831f18,DAT_ffff000090831f1c,&DAT_ffff000090831f1d,1);
    }
  }
  if (DAT_ffff000090831fe8 != 0xfffd) {
    if (DAT_ffff000090831fe8 == 0xfffc) {
      (*DAT_ffff00009087ce28)(DAT_ffff000090831fec);
    }
    else if (DAT_ffff000090831fe8 == 0xfffb) {
      (*DAT_ffff00009087ce20)(DAT_ffff000090831fec);
    }
    else {
      (*DAT_ffff00009087ce58)(DAT_ffff000090831fe8,DAT_ffff000090831fec,&DAT_ffff000090831fed,1);
    }
  }
  if (DAT_ffff0000908320b8 != 0xfffd) {
    if (DAT_ffff0000908320b8 == 0xfffc) {
      (*DAT_ffff00009087ce28)(DAT_ffff0000908320bc);
    }
    else if (DAT_ffff0000908320b8 == 0xfffb) {
      (*DAT_ffff00009087ce20)(DAT_ffff0000908320bc);
    }
    else {
      (*DAT_ffff00009087ce58)(DAT_ffff0000908320b8,DAT_ffff0000908320bc,&DAT_ffff0000908320bd,1);
    }
  }
  FUN_ffff000090785730(0x5a,0);
  FUN_ffff00009078561c(0x5a,1);
  FUN_ffff00009078568c(0x5a,0);
                    /* WARNING: Could not recover jumptable at 0xffff00009076a478. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*DAT_ffff00009087ce28)(10);
  return;
}


