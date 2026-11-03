// ffff00009076aca4  size=332  FUN_ffff00009076aca4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076aca4(uint param_1)

{
  code *UNRECOVERED_JUMPTABLE;
  
  FUN_ffff00009077e11c
            (s__LK_LCM__s_p10u_backlight__level_ffff0000907e394c,s_lcm_setbacklight_ffff0000907e029c
             ,param_1);
  DAT_ffff00009083218d = (ushort)(param_1 >> 8) & 0xff | (ushort)((param_1 & 0xff00ff) << 8);
  if (DAT_ffff000090832188 == 0xfffb) {
    (*_DAT_ffff00009087cd28)(DAT_ffff00009083218c);
  }
  else if (DAT_ffff000090832188 != 0xfffd) {
    if (DAT_ffff000090832188 == 0xfffc) {
      (*_DAT_ffff00009087cd30)(DAT_ffff00009083218c);
    }
    else {
      (*_DAT_ffff00009087cd60)(DAT_ffff000090832188,DAT_ffff00009083218c,&DAT_ffff00009083218d,1);
    }
  }
  if (DAT_ffff000090832258 == 0xfffd) {
    return;
  }
  UNRECOVERED_JUMPTABLE = _DAT_ffff00009087cd30;
  if ((DAT_ffff000090832258 != 0xfffc) &&
     (UNRECOVERED_JUMPTABLE = _DAT_ffff00009087cd28, DAT_ffff000090832258 != 0xfffb)) {
                    /* WARNING: Could not recover jumptable at 0xffff00009076adec. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    (*_DAT_ffff00009087cd60)(DAT_ffff000090832258,DAT_ffff00009083225c,0xffff00009083225d,1);
    return;
  }
                    /* WARNING: Could not recover jumptable at 0xffff00009076adc4. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*UNRECOVERED_JUMPTABLE)(DAT_ffff00009083225c);
  return;
}


