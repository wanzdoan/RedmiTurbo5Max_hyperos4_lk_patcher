// ffff0000907344b4  size=532  FUN_ffff0000907344b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907344b4(code *param_1)

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
  iVar2 = FUN_ffff000090783718
                    (s_tee_shm_ffff0000907e2d57,&DAT_ffff00009086dab0,&DAT_ffff00009086dab8);
  if (iVar2 != 0) {
    FUN_ffff00009077e11c
              (s_MiTEE__Could_not_find__s_from_re_ffff0000907d1176,s_tee_shm_ffff0000907e2d57);
    return;
  }
  _DAT_ffff00009086dab8 = 0x180000;
  _DAT_ffff00009086dab0 = _DAT_ffff00009086dab0 + 0x200000;
  if (_DAT_ffff00009086dab0 != 0) {
    iVar2 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,s_vm_mitee_debug_ffff0000907e6294,0x180000,&local_48,0xc
                       ,_DAT_ffff00009086dab0 & 0xfffffffffffff000,0,0);
    lVar1 = local_48;
    if ((iVar2 == 0) && (local_48 != 0)) {
      uVar7 = FUN_ffff00009077b334(s_key__ffff0000907d92e2);
      uVar8 = FUN_ffff00009077f250(s_key__ffff0000907d92e2);
      uVar3 = (*param_1)(uVar7,uVar8);
      uVar7 = FUN_ffff00009077b334(lVar1 + 0x171);
      uVar4 = (*param_1)(uVar7,0x159);
      uVar7 = FUN_ffff00009077b334(s_log__ffff0000907e4a6c);
      uVar8 = FUN_ffff00009077f250(s_log__ffff0000907e4a6c);
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
          uVar7 = FUN_ffff00009077b334(local_48 + 0x3cb);
          uVar6 = (*param_1)(uVar7,uVar10);
          iVar2 = iVar2 + (uVar6 & 1);
        }
        else {
          uVar7 = FUN_ffff00009077b334(local_48 + 0x3cb + uVar10);
          uVar6 = (*param_1)(uVar7,*(int *)(lVar1 + 8) - uVar3);
          uVar7 = FUN_ffff00009077b334(local_48 + 0x3cb);
          uVar3 = (*param_1)(uVar7,uVar10);
          iVar2 = iVar2 + (uVar3 & 1) + (uVar6 & 1);
        }
      }
      if (iVar2 != 0) {
        return;
      }
      pcVar9 = s_mitee__Log_dump_FAIL_ffff0000907ff139;
    }
    else {
      pcVar9 = s_MiTEE__vmm_alloc_physical_failed_ffff0000907fb88b;
    }
    FUN_ffff00009077e11c(pcVar9);
    return;
  }
  FUN_ffff00009077e11c(s_MiTEE__invalid_debug_buffer_ffff000090800bb0);
  return;
}


