
void FUN_ffff00009076a044(void)

{
  if (DAT_ffff000090831b08 == 0xfffb) {
    (*DAT_ffff00009087cd28)(DAT_ffff000090831b0c);
  }
  else if (DAT_ffff000090831b08 != 0xfffd) {
    if (DAT_ffff000090831b08 == 0xfffc) {
      (*DAT_ffff00009087cd30)(DAT_ffff000090831b0c);
    }
    else {
      (*DAT_ffff00009087cd60)(DAT_ffff000090831b08,DAT_ffff000090831b0c,&DAT_ffff000090831b0d,1);
    }
  }
  if (DAT_ffff000090831bd8 != 0xfffd) {
    if (DAT_ffff000090831bd8 == 0xfffc) {
      (*DAT_ffff00009087cd30)(DAT_ffff000090831bdc);
    }
    else if (DAT_ffff000090831bd8 == 0xfffb) {
      (*DAT_ffff00009087cd28)(DAT_ffff000090831bdc);
    }
    else {
      (*DAT_ffff00009087cd60)(DAT_ffff000090831bd8,DAT_ffff000090831bdc,&DAT_ffff000090831bdd,1);
    }
  }
  if (DAT_ffff000090831ca8 != 0xfffd) {
    if (DAT_ffff000090831ca8 == 0xfffc) {
      (*DAT_ffff00009087cd30)(DAT_ffff000090831cac);
    }
    else if (DAT_ffff000090831ca8 == 0xfffb) {
      (*DAT_ffff00009087cd28)(DAT_ffff000090831cac);
    }
    else {
      (*DAT_ffff00009087cd60)(DAT_ffff000090831ca8,DAT_ffff000090831cac,&DAT_ffff000090831cad,1);
    }
  }
  if (DAT_ffff000090831d78 != 0xfffd) {
    if (DAT_ffff000090831d78 == 0xfffc) {
      (*DAT_ffff00009087cd30)(DAT_ffff000090831d7c);
    }
    else if (DAT_ffff000090831d78 == 0xfffb) {
      (*DAT_ffff00009087cd28)(DAT_ffff000090831d7c);
    }
    else {
      (*DAT_ffff00009087cd60)(DAT_ffff000090831d78,DAT_ffff000090831d7c,&DAT_ffff000090831d7d,1);
    }
  }
  FUN_ffff000090785730(0x5a,0);
  FUN_ffff00009078561c(0x5a,1);
  FUN_ffff00009078568c(0x5a,0);
                    /* WARNING: Could not recover jumptable at 0xffff00009076a25c. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*DAT_ffff00009087cd30)(10);
  return;
}


