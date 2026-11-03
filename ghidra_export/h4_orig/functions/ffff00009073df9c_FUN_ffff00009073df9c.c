// ffff00009073df9c  size=640  FUN_ffff00009073df9c

void FUN_ffff00009073df9c(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  int local_24;
  long local_8;
  
  local_8 = 0;
  local_24 = 0;
  iVar1 = FUN_ffff00009073ddc4(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b334();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077e11c(s_lastpc_dump_failed__ffff0000907efc7c);
      }
    }
    FUN_ffff00009073de5c(&local_8);
  }
  FUN_ffff00009077e11c(s_lastpc_done_ffff0000907d475b);
  iVar1 = FUN_ffff00009074b4fc(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b334();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077e11c(s_plat_dfd_mcu_dump_failed__ffff0000907f4ae0);
      }
    }
    FUN_ffff00009074b558(&local_8);
  }
  FUN_ffff00009077e11c(s_plat_dfd_mcu_done_ffff0000907e4be8);
  iVar1 = FUN_ffff00009073e21c(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b334();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077e11c(s_return_stack_dump_failed__ffff0000907ec75d);
      }
    }
    FUN_ffff00009073e2d4(&local_8);
  }
  FUN_ffff00009077e11c(s_return_stack_done_ffff0000907f15c3);
  iVar1 = FUN_ffff00009073e61c(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b334();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077e11c(s_tracker_dump_failed__ffff000090800ce8);
      }
    }
    FUN_ffff00009073ec0c(&local_8);
  }
  FUN_ffff00009077e11c(s_tracker_done_ffff0000907e7b7f);
  iVar1 = FUN_ffff00009073d830(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b334();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077e11c(s_lastbus_dump_failed__ffff0000907eae85);
      }
    }
    FUN_ffff00009073dbe8(&local_8);
  }
  FUN_ffff00009077e11c(s_lastbus_done_ffff0000907f8434);
  iVar1 = FUN_ffff00009073cf64(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b334();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077e11c(s_etb_dump_failed__ffff0000907efc91);
      }
    }
    FUN_ffff00009073d19c(&local_8);
  }
  FUN_ffff00009077e11c(s_etb_done_ffff0000907f8442);
  iVar1 = FUN_ffff00009073ac38(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b334();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077e11c(s_err_flag_dump_failed__ffff0000907dc97c);
      }
    }
    FUN_ffff00009073b174(&local_8);
  }
  FUN_ffff00009077e11c(s_err_flag_done_ffff0000907fd672);
  return;
}


