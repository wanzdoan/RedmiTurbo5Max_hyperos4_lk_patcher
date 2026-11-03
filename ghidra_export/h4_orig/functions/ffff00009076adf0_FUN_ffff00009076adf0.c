// ffff00009076adf0  size=332  FUN_ffff00009076adf0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076adf0(uint param_1)

{
  code *UNRECOVERED_JUMPTABLE;
  
  FUN_ffff00009077e11c
            (s__LK_LCM__s_p10u_backlight__level_ffff0000907e394c,s_lcm_setbacklight_ffff0000907e029c
             ,param_1);
  DAT_ffff00009083232d = (ushort)(param_1 >> 8) & 0xff | (ushort)((param_1 & 0xff00ff) << 8);
  if (DAT_ffff000090832328 == 0xfffb) {
    (*_DAT_ffff00009087ce20)(DAT_ffff00009083232c);
  }
  else if (DAT_ffff000090832328 != 0xfffd) {
    if (DAT_ffff000090832328 == 0xfffc) {
      (*_DAT_ffff00009087ce28)(DAT_ffff00009083232c);
    }
    else {
      (*_DAT_ffff00009087ce58)(DAT_ffff000090832328,DAT_ffff00009083232c,&DAT_ffff00009083232d,1);
    }
  }
  if (DAT_ffff0000908323f8 == 0xfffd) {
    return;
  }
  UNRECOVERED_JUMPTABLE = _DAT_ffff00009087ce28;
  if ((DAT_ffff0000908323f8 != 0xfffc) &&
     (UNRECOVERED_JUMPTABLE = _DAT_ffff00009087ce20, DAT_ffff0000908323f8 != 0xfffb)) {
                    /* WARNING: Could not recover jumptable at 0xffff00009076af38. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    (*_DAT_ffff00009087ce58)(DAT_ffff0000908323f8,DAT_ffff0000908323fc,0xffff0000908323fd,1);
    return;
  }
                    /* WARNING: Could not recover jumptable at 0xffff00009076af10. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*UNRECOVERED_JUMPTABLE)(DAT_ffff0000908323fc);
  return;
}


