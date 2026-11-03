// ffff00009076ab28  size=332  FUN_ffff00009076ab28

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076ab28(uint param_1)

{
  code *UNRECOVERED_JUMPTABLE;
  
  FUN_ffff00009077de54
            (s__LK_LCM__s_p10u_backlight__level_ffff0000907e387b,s_lcm_setbacklight_ffff0000907e01f9
             ,param_1);
  DAT_ffff00009083230d = (ushort)(param_1 >> 8) & 0xff | (ushort)((param_1 & 0xff00ff) << 8);
  if (DAT_ffff000090832308 == 0xfffb) {
    (*_DAT_ffff00009087cd88)(DAT_ffff00009083230c);
  }
  else if (DAT_ffff000090832308 != 0xfffd) {
    if (DAT_ffff000090832308 == 0xfffc) {
      (*_DAT_ffff00009087cd90)(DAT_ffff00009083230c);
    }
    else {
      (*_DAT_ffff00009087cdc0)(DAT_ffff000090832308,DAT_ffff00009083230c,&DAT_ffff00009083230d,1);
    }
  }
  if (DAT_ffff0000908323d8 == 0xfffd) {
    return;
  }
  UNRECOVERED_JUMPTABLE = _DAT_ffff00009087cd90;
  if ((DAT_ffff0000908323d8 != 0xfffc) &&
     (UNRECOVERED_JUMPTABLE = _DAT_ffff00009087cd88, DAT_ffff0000908323d8 != 0xfffb)) {
                    /* WARNING: Could not recover jumptable at 0xffff00009076ac70. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    (*_DAT_ffff00009087cdc0)(DAT_ffff0000908323d8,DAT_ffff0000908323dc,0xffff0000908323dd,1);
    return;
  }
                    /* WARNING: Could not recover jumptable at 0xffff00009076ac48. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*UNRECOVERED_JUMPTABLE)(DAT_ffff0000908323dc);
  return;
}


