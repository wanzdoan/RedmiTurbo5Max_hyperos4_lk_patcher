
char * FUN_ffff0000907c281c(uint param_1)

{
  undefined8 uVar1;
  
  if (param_1 < 6) {
    return &DAT_ffff0000908146bc + *(int *)(&DAT_ffff0000908146bc + (long)(int)param_1 * 4);
  }
  uVar1 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
  FUN_ffff0000907c0ee8
            (uVar1,&DAT_ffff0000907ebfa6,&DAT_ffff0000907f91de,s___ERROR__ffff0000907e07dc,
             s_Unknown_AvbVBMetaVerifyResult_va_ffff0000907faec2,0);
  return s__unknown__ffff0000907e8aab;
}


