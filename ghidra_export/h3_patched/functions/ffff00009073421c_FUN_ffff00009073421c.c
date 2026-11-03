// ffff00009073421c  size=532  FUN_ffff00009073421c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073421c(code *param_1)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  char *pcVar9;
  ulong uVar10;
  long local_48;
  
  local_48 = 0;
  iVar2 = FUN_ffff000090783450
                    (s_tee_shm_ffff0000907e2c6f,&DAT_ffff00009086da18,&DAT_ffff00009086da20);
  if (iVar2 != 0) {
    FUN_ffff00009077de54
              (s_MiTEE__Could_not_find__s_from_re_ffff0000907d1139,s_tee_shm_ffff0000907e2c6f);
    return;
  }
  _DAT_ffff00009086da20 = 0x180000;
  _DAT_ffff00009086da18 = _DAT_ffff00009086da18 + 0x200000;
  if (_DAT_ffff00009086da18 != 0) {
    iVar2 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_vm_mitee_debug_ffff0000907e6169,0x180000,&local_48,0xc
                       ,_DAT_ffff00009086da18 & 0xfffffffffffff000,0,0);
    lVar1 = local_48;
    if ((iVar2 == 0) && (local_48 != 0)) {
      uVar7 = FUN_ffff00009077b06c(s_key__ffff0000907d924b);
      uVar8 = FUN_ffff00009077ef88(s_key__ffff0000907d924b);
      uVar3 = (*param_1)(uVar7,uVar8);
      uVar7 = FUN_ffff00009077b06c(lVar1 + 0x171);
      uVar4 = (*param_1)(uVar7,0x159);
      uVar7 = FUN_ffff00009077b06c(s_log__ffff0000907e4951);
      uVar8 = FUN_ffff00009077ef88(s_log__ffff0000907e4951);
      uVar5 = (*param_1)(uVar7,uVar8);
      uVar6 = *(uint *)(lVar1 + 8);
      iVar2 = (uVar4 & 1) + (uVar3 & 1) + (uVar5 & 1);
      if (uVar6 != 0) {
        uVar3 = 0;
        if (uVar6 != 0) {
          uVar3 = *(uint *)(lVar1 + 4) / uVar6;
        }
        uVar3 = *(uint *)(lVar1 + 4) - uVar3 * uVar6;
        uVar10 = (ulong)uVar3;
        if (*(uint *)(lVar1 + 4) < uVar6) {
          uVar7 = FUN_ffff00009077b06c(local_48 + 0x3cb);
          uVar6 = (*param_1)(uVar7,uVar10);
          iVar2 = iVar2 + (uVar6 & 1);
        }
        else {
          uVar7 = FUN_ffff00009077b06c(local_48 + 0x3cb + uVar10);
          uVar6 = (*param_1)(uVar7,*(int *)(lVar1 + 8) - uVar3);
          uVar7 = FUN_ffff00009077b06c(local_48 + 0x3cb);
          uVar3 = (*param_1)(uVar7,uVar10);
          iVar2 = iVar2 + (uVar3 & 1) + (uVar6 & 1);
        }
      }
      if (iVar2 != 0) {
        return;
      }
      pcVar9 = s_mitee__Log_dump_FAIL_ffff0000907fef91;
    }
    else {
      pcVar9 = s_MiTEE__vmm_alloc_physical_failed_ffff0000907fb6ef;
    }
    FUN_ffff00009077de54(pcVar9);
    return;
  }
  FUN_ffff00009077de54(s_MiTEE__invalid_debug_buffer_ffff0000908009d4);
  return;
}


