// ffff000090783450  size=468  FUN_ffff000090783450

undefined4 FUN_ffff000090783450(undefined8 param_1,undefined8 *param_2,undefined8 *param_3)

{
  long lVar1;
  int iVar2;
  int iVar3;
  undefined8 *puVar4;
  undefined4 uVar5;
  ulong uVar6;
  int *piVar7;
  long local_38;
  
  local_38 = 0;
  puVar4 = (undefined8 *)FUN_ffff00009071a204(s_aee_lk_ffff000090802cf8,0);
  if (puVar4 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__fail_to_get__s_reserved_i_ffff0000907fc34f,
               s_mrdump_get_reserved_info_ffff0000907d01ff,200,s_aee_lk_ffff000090802cf8);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_aee_mrd_ffff0000907fe0ac,0xc9,&DAT_ffff0000907d24c9);
  }
  lVar1 = puVar4[1];
  iVar2 = FUN_ffff00009077b79c
                    (DAT_ffff000090839908,s_vm_mblock_reserved_info_ffff0000907ffbfa,
                     lVar1 + 0xfffU & 0xfffffffffffff000,&local_38,0xc,*puVar4,0,0);
  if (iVar2 != 0) {
    FUN_ffff00009077de54
              (s__s__d__vm_mblock_reserved_info_v_ffff0000907e9e8b,
               s_mrdump_get_reserved_info_ffff0000907d01ff,0xd7);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_aee_mrd_ffff0000907fe0ac,0xd8,&DAT_ffff0000907d24c9);
  }
  lVar1 = local_38 + lVar1;
  piVar7 = (int *)(lVar1 + -0x8000);
  iVar2 = *piVar7;
  *piVar7 = -1;
  iVar3 = FUN_ffff000090781b6c(piVar7,0x4c08);
  if (iVar2 == iVar3) {
    uVar6 = 0;
    puVar4 = (undefined8 *)(lVar1 + -0x7ff8);
    *piVar7 = iVar2;
    do {
      iVar2 = FUN_ffff00009077f080(param_1,(long)puVar4 + 0x14,0x80);
      if (iVar2 == 0) {
        *param_2 = *puVar4;
        *param_3 = puVar4[1];
        FUN_ffff00009077de54
                  (s__s__d__mrdump_show__mblock_R__d__ffff0000907d0218,
                   s_mrdump_get_reserved_info_ffff0000907d01ff,0xf5,uVar6 & 0xffffffff,*puVar4,
                   puVar4[1],(long)puVar4 + 0x14);
        break;
      }
      uVar6 = uVar6 + 1;
      puVar4 = puVar4 + 0x13;
    } while (uVar6 != 0x80);
    uVar5 = 0;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__crc32_check_fail_0x_x____0_ffff000090802cff,
               s_mrdump_get_reserved_info_ffff0000907d01ff,0xe8,iVar3,iVar2);
    uVar5 = 0xffffffff;
  }
  FUN_ffff00009077bb68(DAT_ffff000090839908,local_38);
  return uVar5;
}


