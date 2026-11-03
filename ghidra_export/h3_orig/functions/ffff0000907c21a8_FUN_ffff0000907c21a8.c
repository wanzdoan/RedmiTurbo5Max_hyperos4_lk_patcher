// ffff0000907c21a8  size=32  FUN_ffff0000907c21a8

char * FUN_ffff0000907c21a8(uint param_1)

{
  undefined8 uVar1;
  
  if (param_1 < 6) {
    return &DAT_ffff000090814494 + *(int *)(&DAT_ffff000090814494 + (long)(int)param_1 * 4);
  }
  uVar1 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8979);
  FUN_ffff0000907c0874
            (uVar1,&DAT_ffff0000907ebe23,&DAT_ffff0000907f9042,s___ERROR__ffff0000907e0739,
             s_Unknown_AvbVBMetaVerifyResult_va_ffff0000907fad26,0);
  return s__unknown__ffff0000907e8967;
}


