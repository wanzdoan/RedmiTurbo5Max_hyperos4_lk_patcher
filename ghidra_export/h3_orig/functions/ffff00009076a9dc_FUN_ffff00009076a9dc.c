// ffff00009076a9dc  size=332  FUN_ffff00009076a9dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076a9dc(uint param_1)

{
  code *UNRECOVERED_JUMPTABLE;
  
  FUN_ffff00009077de54
            (s__LK_LCM__s_p10u_backlight__level_ffff0000907e387b,s_lcm_setbacklight_ffff0000907e01f9
             ,param_1);
  DAT_ffff00009083216d = (ushort)(param_1 >> 8) & 0xff | (ushort)((param_1 & 0xff00ff) << 8);
  if (DAT_ffff000090832168 == 0xfffb) {
    (*_DAT_ffff00009087cc90)(DAT_ffff00009083216c);
  }
  else if (DAT_ffff000090832168 != 0xfffd) {
    if (DAT_ffff000090832168 == 0xfffc) {
      (*_DAT_ffff00009087cc98)(DAT_ffff00009083216c);
    }
    else {
      (*_DAT_ffff00009087ccc8)(DAT_ffff000090832168,DAT_ffff00009083216c,&DAT_ffff00009083216d,1);
    }
  }
  if (DAT_ffff000090832238 == 0xfffd) {
    return;
  }
  UNRECOVERED_JUMPTABLE = _DAT_ffff00009087cc98;
  if ((DAT_ffff000090832238 != 0xfffc) &&
     (UNRECOVERED_JUMPTABLE = _DAT_ffff00009087cc90, DAT_ffff000090832238 != 0xfffb)) {
                    /* WARNING: Could not recover jumptable at 0xffff00009076ab24. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    (*_DAT_ffff00009087ccc8)(DAT_ffff000090832238,DAT_ffff00009083223c,0xffff00009083223d,1);
    return;
  }
                    /* WARNING: Could not recover jumptable at 0xffff00009076aafc. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*UNRECOVERED_JUMPTABLE)(DAT_ffff00009083223c);
  return;
}


