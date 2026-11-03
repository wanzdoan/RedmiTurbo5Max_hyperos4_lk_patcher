// ffff00009079771c  size=144  FUN_ffff00009079771c

void FUN_ffff00009079771c(code *param_1)

{
  int iVar1;
  ulong uVar2;
  long local_28;
  long local_8;
  
  local_8 = 0;
  local_28 = 0;
  iVar1 = FUN_ffff000090783718(s_me_cmdq_reserved_ffff0000907fe4b2,&local_8,&local_28);
  if ((iVar1 < 0) || (local_8 == 0)) {
    FUN_ffff00009077e11c
              (s__cmdq__err___s__get_mblock_reser_ffff0000907f095c,
               s_cmdq_save_debug_info_ffff0000907e3d49);
  }
  else {
    uVar2 = (*param_1)(local_8 + local_28 + -0x90000,0x90000);
    if ((uVar2 & 1) == 0) {
      FUN_ffff00009077e11c(s__cmdq__err__AEE_EXPDB_dump_fail_ffff0000907e05f2);
    }
  }
  return;
}


