// File offset entry: 0x4fd24

undefined4 FUN_ffff0000b804fd24(undefined8 param_1,undefined8 *param_2,undefined8 *param_3)

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
  puVar4 = (undefined8 *)FUN_ffff0000b800db20(s_aee_lk_ffff0000b80a042c,0);
  if (puVar4 == (undefined8 *)0x0) {
    FUN_ffff0000b804beb0
              (s__s__d__fail_to_get__s_reserved_i_ffff0000b809ccdd,
               s_mrdump_get_reserved_info_ffff0000b80849ff,200,s_aee_lk_ffff0000b80a042c);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4
              (s_platform_mediatek_common_aee_mrd_ffff0000b809dce5,0xc9,&DAT_ffff0000b8085ec6);
  }
  lVar1 = puVar4[1];
  iVar2 = FUN_ffff0000b804a16c
                    (PTR_DAT_ffff0000b8107610,s_vm_mblock_reserved_info_ffff0000b809ec34,
                     lVar1 + 0xfffU & 0xfffffffffffff000,&local_38,0xc,*puVar4,0,0);
  if (iVar2 != 0) {
    FUN_ffff0000b804beb0
              (s__s__d__vm_mblock_reserved_info_v_ffff0000b8092bfa,
               s_mrdump_get_reserved_info_ffff0000b80849ff,0xd7);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4
              (s_platform_mediatek_common_aee_mrd_ffff0000b809dce5,0xd8,&DAT_ffff0000b8085ec6);
  }
  lVar1 = local_38 + lVar1;
  piVar7 = (int *)(lVar1 + -0x8000);
  iVar2 = *piVar7;
  *piVar7 = -1;
  iVar3 = FUN_ffff0000b804f15c(piVar7,0x4c08);
  if (iVar2 == iVar3) {
    uVar6 = 0;
    puVar4 = (undefined8 *)(lVar1 + -0x7ff8);
    *piVar7 = iVar2;
    do {
      iVar2 = FUN_ffff0000b804d038(param_1,(long)puVar4 + 0x14,0x80);
      if (iVar2 == 0) {
        *param_2 = *puVar4;
        *param_3 = puVar4[1];
        FUN_ffff0000b804beb0
                  (s__s__d__mrdump_show__mblock_R__d__ffff0000b8084a18,
                   s_mrdump_get_reserved_info_ffff0000b80849ff,0xf5,uVar6 & 0xffffffff,*puVar4,
                   puVar4[1],(long)puVar4 + 0x14);
        break;
      }
      uVar6 = uVar6 + 1;
      puVar4 = puVar4 + 0x13;
    } while (uVar6 != 0x80);
    uVar5 = 0;
  }
  else {
    FUN_ffff0000b804beb0
              (s__s__d__crc32_check_fail_0x_x____0_ffff0000b80a0433,
               s_mrdump_get_reserved_info_ffff0000b80849ff,0xe8,iVar3,iVar2);
    uVar5 = 0xffffffff;
  }
  FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,local_38);
  return uVar5;
}


