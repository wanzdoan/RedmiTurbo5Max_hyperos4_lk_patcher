
bool FUN_ffff0000907c112c(ulong *param_1,long param_2)

{
  bool bVar1;
  ulong uVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  ulong uVar6;
  
  uVar6 = *param_1;
  uVar2 = uVar6 + param_2;
  *param_1 = uVar2;
  if (uVar2 < uVar6) {
    pcVar5 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
    lVar3 = thunk_FUN_ffff00009077f250(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
    lVar4 = lVar3 + -2;
    if (-1 < lVar3 + -2) {
      do {
        if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar4] == '/') {
          pcVar5 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar4 + 1;
          break;
        }
        bVar1 = 0 < lVar4;
        lVar4 = lVar4 + -1;
      } while (bVar1);
    }
    FUN_ffff0000907c0ee8
              (pcVar5,&DAT_ffff0000907ebfa6,&DAT_ffff000090801b7b,s___ERROR__ffff0000907e07dc,
               s_Overflow_when_adding_values__ffff00009080364e,0);
  }
  return uVar6 <= uVar2;
}


