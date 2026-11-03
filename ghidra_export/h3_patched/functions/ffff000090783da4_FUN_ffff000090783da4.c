// ffff000090783da4  size=420  FUN_ffff000090783da4

void FUN_ffff000090783da4(void)

{
  long lVar1;
  undefined8 uVar2;
  int iVar3;
  undefined4 uVar4;
  undefined8 *puVar5;
  undefined8 *puVar6;
  ulong uVar7;
  undefined4 *puVar8;
  ulong uVar9;
  long local_48;
  
  local_48 = 0;
  puVar5 = (undefined8 *)FUN_ffff00009071a204(s_aee_lk_ffff000090802cf8,0);
  if (puVar5 != (undefined8 *)0x0) {
    lVar1 = puVar5[1];
    uVar7 = lVar1 + 0xfffU & 0xfffffffffffff000;
    iVar3 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_vm_mblock_reserved_info_ffff0000907ffbfa,uVar7,
                       &local_48,0xc,*puVar5,0,0);
    if (iVar3 == 0) {
      lVar1 = local_48 + lVar1;
      puVar8 = (undefined4 *)(lVar1 + -0x8000);
      FUN_ffff00009077eb88(puVar8,0,0x8000);
      uVar9 = 0;
      iVar3 = 0;
      puVar5 = (undefined8 *)(lVar1 + -0x7ff8);
      do {
        puVar6 = (undefined8 *)FUN_ffff00009071ae48(uVar9 & 0xffffffff);
        if (puVar6 != (undefined8 *)0x0) {
          uVar2 = puVar6[1];
          uVar4 = *(undefined4 *)(puVar6 + 2);
          iVar3 = iVar3 + 1;
          *puVar5 = *puVar6;
          puVar5[1] = uVar2;
          *(undefined4 *)(puVar5 + 2) = uVar4;
          FUN_ffff00009077efe4((long)puVar5 + 0x14,(long)puVar6 + 0x14,0x7f);
        }
        uVar9 = uVar9 + 1;
        puVar5 = puVar5 + 0x13;
      } while (uVar9 != 0x80);
      *(int *)(lVar1 + -0x7ffc) = iVar3;
      FUN_ffff00009077de54
                (s__s__d__mblock_query_finished__to_ffff0000907dd2be,
                 s_mrdump_save_reserved_info_ffff0000907d37bb,0x7d,iVar3);
      *puVar8 = 0xffffffff;
      uVar4 = FUN_ffff000090781b6c(puVar8,0x4c08);
      *puVar8 = uVar4;
      FUN_ffff000090707ce4(local_48,uVar7);
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_48);
    }
    else {
      FUN_ffff00009077de54(s_vm_mblock_reserved_info_vmm_allo_ffff0000907e6b6e);
    }
    return;
  }
  FUN_ffff00009077de54
            (s__s__d___s_reserved_memroy_query_f_ffff0000907db7d3,
             s_mrdump_save_reserved_info_ffff0000907d37bb,0x4e,s_aee_lk_ffff000090802cf8);
  return;
}


