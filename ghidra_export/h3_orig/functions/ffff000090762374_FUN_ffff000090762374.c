// ffff000090762374  size=184  FUN_ffff000090762374

int FUN_ffff000090762374(undefined8 param_1)

{
  int iVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff000090719218();
  lVar3 = FUN_ffff00009071922c();
  if ((lVar2 == 0) || (lVar3 == 0)) {
    FUN_ffff00009077de54
              (s__s__d____s___pllk_log_header_is_n_ffff0000907fbf1e,
               s_GetAndWritePLLKlog_ffff0000907fddb4,0x451,s_BLACKBOX_ffff0000907d9988,lVar2);
  }
  else {
    iVar1 = *(int *)(lVar2 + 0x4c) + *(int *)(lVar2 + 0x48) + *(int *)(lVar2 + 0x58);
    uVar4 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,lVar3,param_1,iVar1);
    if ((uVar4 & 1) != 0) {
      return iVar1;
    }
    FUN_ffff00009077de54
              (s__s__d____s___writePartitionInfo_f_ffff0000907de85c,
               s_GetAndWritePLLKlog_ffff0000907fddb4,0x457,s_BLACKBOX_ffff0000907d9988);
  }
  return 0;
}


