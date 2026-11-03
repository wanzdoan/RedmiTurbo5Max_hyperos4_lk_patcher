// ffff000090741f78  size=384  FUN_ffff000090741f78

void FUN_ffff000090741f78(code *param_1)

{
  uint uVar1;
  int iVar2;
  undefined4 uVar3;
  ulong *puVar4;
  long lVar5;
  undefined8 uVar6;
  ulong uVar7;
  ulong uVar8;
  long local_38;
  
  local_38 = 0;
  puVar4 = (ulong *)FUN_ffff00009071a204(s_atf_log_reserved_ffff0000907daea9,0);
  if (puVar4 == (ulong *)0x0) {
    FUN_ffff00009077de54
              (s_TFA__Could_not_find__s_from_mblo_ffff0000907ec69c,
               s_atf_log_reserved_ffff0000907daea9);
    return;
  }
  if ((*puVar4 == 0) || (puVar4[1] == 0)) {
    FUN_ffff00009077de54(s_TFA__Fail_to_allocate_debug_buff_ffff0000907e2f6f);
    return;
  }
  uVar8 = puVar4[1] + 0xfff & 0xfffffffffffff000;
  iVar2 = FUN_ffff00009077b79c
                    (DAT_ffff000090839908,s_vm_tfa_debug_ffff0000907f6506,uVar8,&local_38,0xc,
                     *puVar4 & 0xfffffffffffff000,0,0);
  if ((iVar2 == 0) && (local_38 != 0)) {
    lVar5 = FUN_ffff00009077d24c(0x100000);
    if (lVar5 != 0) {
      do {
        FUN_ffff00009077eb88(lVar5,0,0x100000);
        uVar3 = FUN_ffff000090741d3c(local_38,lVar5,0x100000,0x100000);
        uVar6 = FUN_ffff00009077b06c(lVar5);
        uVar7 = (*param_1)(uVar6,uVar3);
        if ((uVar7 & 1) == 0) {
          FUN_ffff00009077de54(s_TFA__Log_dump_FAIL_ffff0000907f6513);
          break;
        }
        uVar1 = (int)uVar8 - 0x100000;
        uVar8 = (ulong)uVar1;
        local_38 = local_38 + 0x100000;
      } while (0 < (int)uVar1);
      thunk_FUN_ffff0000907c2ad8(lVar5);
    }
    FUN_ffff00009077bb68(DAT_ffff000090839908,local_38);
  }
  else {
    FUN_ffff00009077de54(s_TFA__vmm_alloc_physical_failed_ffff0000907fd5b9);
  }
  return;
}


