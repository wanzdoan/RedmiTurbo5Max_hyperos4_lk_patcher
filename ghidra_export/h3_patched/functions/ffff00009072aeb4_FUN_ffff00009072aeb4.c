// ffff00009072aeb4  size=104  FUN_ffff00009072aeb4

void FUN_ffff00009072aeb4(code *param_1)

{
  long lVar1;
  ulong uVar2;
  
  lVar1 = FUN_ffff000090719218();
  if (((lVar1 != 0) && (*(long *)(lVar1 + 0x14) != 0)) &&
     (uVar2 = (*param_1)(*(long *)(lVar1 + 0x14),
                         *(int *)(lVar1 + 0x58) + *(int *)(lVar1 + 0x4c) + *(int *)(lVar1 + 0x48)),
     (uVar2 & 1) == 0)) {
    FUN_ffff00009077de54(s_pllk_log_dump_fail_ffff0000907ec1b3);
    return;
  }
  return;
}


