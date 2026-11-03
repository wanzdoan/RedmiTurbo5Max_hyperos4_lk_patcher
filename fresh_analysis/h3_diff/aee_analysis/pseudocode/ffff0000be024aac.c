
void FUN_ffff0000be024aac(code *param_1)

{
  long lVar1;
  ulong uVar2;
  
  lVar1 = FUN_ffff0000be01be34();
  if (((lVar1 != 0) && (*(long *)(lVar1 + 0x14) != 0)) &&
     (uVar2 = (*param_1)(*(long *)(lVar1 + 0x14),
                         *(int *)(lVar1 + 0x58) + *(int *)(lVar1 + 0x4c) + *(int *)(lVar1 + 0x48)),
     (uVar2 & 1) == 0)) {
    FUN_ffff0000be05fd98(s_pllk_log_dump_fail_ffff0000be0b766c);
    return;
  }
  return;
}


