// ffff00009073dd04  size=640  FUN_ffff00009073dd04

void FUN_ffff00009073dd04(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  int local_24;
  long local_8;
  
  local_8 = 0;
  local_24 = 0;
  iVar1 = FUN_ffff00009073db2c(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b06c();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54(s_lastpc_dump_failed__ffff0000907efaf4);
      }
    }
    FUN_ffff00009073dbc4(&local_8);
  }
  FUN_ffff00009077de54(s_lastpc_done_ffff0000907d471e);
  iVar1 = FUN_ffff00009074b264(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b06c();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54(s_plat_dfd_mcu_dump_failed__ffff0000907f495c);
      }
    }
    FUN_ffff00009074b2c0(&local_8);
  }
  FUN_ffff00009077de54(s_plat_dfd_mcu_done_ffff0000907e4acd);
  iVar1 = FUN_ffff00009073df84(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b06c();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54(s_return_stack_dump_failed__ffff0000907ec5c3);
      }
    }
    FUN_ffff00009073e03c(&local_8);
  }
  FUN_ffff00009077de54(s_return_stack_done_ffff0000907f143b);
  iVar1 = FUN_ffff00009073e384(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b06c();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54(s_tracker_dump_failed__ffff000090800b0c);
      }
    }
    FUN_ffff00009073e974(&local_8);
  }
  FUN_ffff00009077de54(s_tracker_done_ffff0000907e7a24);
  iVar1 = FUN_ffff00009073d598(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b06c();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54(s_lastbus_dump_failed__ffff0000907eaceb);
      }
    }
    FUN_ffff00009073d950(&local_8);
  }
  FUN_ffff00009077de54(s_lastbus_done_ffff0000907f8298);
  iVar1 = FUN_ffff00009073cccc(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b06c();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54(s_etb_dump_failed__ffff0000907efb09);
      }
    }
    FUN_ffff00009073cf04(&local_8);
  }
  FUN_ffff00009077de54(s_etb_done_ffff0000907f82a6);
  iVar1 = FUN_ffff00009073a9a0(&local_8,&local_24);
  if ((iVar1 != 0) && (local_8 != 0)) {
    if (0 < local_24) {
      uVar2 = FUN_ffff00009077b06c();
      uVar3 = (*param_1)(uVar2,(long)local_24);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54(s_err_flag_dump_failed__ffff0000907dc88f);
      }
    }
    FUN_ffff00009073aedc(&local_8);
  }
  FUN_ffff00009077de54(s_err_flag_done_ffff0000907fd4ca);
  return;
}


