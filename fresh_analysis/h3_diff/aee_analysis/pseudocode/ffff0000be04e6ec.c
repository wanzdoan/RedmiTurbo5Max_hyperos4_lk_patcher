
int FUN_ffff0000be04e6ec(undefined8 param_1)

{
  int iVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff0000be01be34();
  lVar3 = FUN_ffff0000be01be48();
  if ((lVar2 == 0) || (lVar3 == 0)) {
    FUN_ffff0000be05fd98
              (s__s__d____s___pllk_log_header_is_n_ffff0000be0c215c,
               s_GetAndWritePLLKlog_ffff0000be0c3486,0x451,s_BLACKBOX_ffff0000be0aa9ee,lVar2);
  }
  else {
    iVar1 = *(int *)(lVar2 + 0x4c) + *(int *)(lVar2 + 0x48) + *(int *)(lVar2 + 0x58);
    uVar4 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,lVar3,param_1,iVar1);
    if ((uVar4 & 1) != 0) {
      return iVar1;
    }
    FUN_ffff0000be05fd98
              (s__s__d____s___writePartitionInfo_f_ffff0000be0add26,
               s_GetAndWritePLLKlog_ffff0000be0c3486,0x457,s_BLACKBOX_ffff0000be0aa9ee);
  }
  return 0;
}


