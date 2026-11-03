
undefined8 FUN_ffff0000be06534c(uint param_1,undefined8 param_2,long *param_3,undefined8 *param_4)

{
  int iVar1;
  int iVar2;
  undefined8 *puVar3;
  long lVar4;
  long lVar5;
  int *piVar6;
  long local_38;
  
  local_38 = 0;
  if (param_1 < 0x80) {
    puVar3 = (undefined8 *)FUN_ffff0000be01cb9c(s_aee_lk_ffff0000be0c695e,0);
    if (puVar3 == (undefined8 *)0x0) {
      FUN_ffff0000be05fd98
                (s__s__d__fail_to_get__s_reserved_i_ffff0000be0c23ce,
                 s_mrdump_get_reserved_info_by_inde_ffff0000be0c364d,0x88,s_aee_lk_ffff0000be0c695e)
      ;
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_common_aee_mrd_ffff0000be0c366f,0x89,&DAT_ffff0000be0a553d);
    }
    lVar4 = puVar3[1];
    iVar1 = FUN_ffff0000be05d9a8
                      (PTR_DAT_ffff0000be0f5570,s_vm_mblock_reserved_info_ffff0000be0c4866,
                       lVar4 + 0xfffU & 0xfffffffffffff000,&local_38,0xc,*puVar3,0,0);
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98
                (s__s__d__vm_mblock_reserved_info_v_ffff0000be0b5b71,
                 s_mrdump_get_reserved_info_by_inde_ffff0000be0c364d,0x97);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_common_aee_mrd_ffff0000be0c366f,0x98,&DAT_ffff0000be0a553d);
    }
    lVar4 = local_38 + lVar4;
    piVar6 = (int *)(lVar4 + -0x8000);
    iVar1 = *piVar6;
    *piVar6 = -1;
    iVar2 = FUN_ffff0000be063f84(piVar6,0x4c08);
    if (iVar1 == iVar2) {
      *piVar6 = iVar1;
      lVar4 = lVar4 + -0x7ff8;
      lVar5 = *(long *)(lVar4 + (ulong)param_1 * 0x98);
      if (lVar5 != 0) {
        *param_3 = lVar5;
        lVar4 = lVar4 + (ulong)param_1 * 0x98;
        *param_4 = *(undefined8 *)(lVar4 + 8);
        FUN_ffff0000be060eb8(param_2,lVar4 + 0x14,0x80);
        FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_38);
        return 0;
      }
    }
    else {
      FUN_ffff0000be05fd98
                (s__s__d__crc32_check_fail_0x_x____0_ffff0000be0c6965,
                 s_mrdump_get_reserved_info_by_inde_ffff0000be0c364d,0xa8,iVar2,iVar1);
    }
    FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_38);
  }
  else {
    FUN_ffff0000be05fd98
              (s__s__d__index_i_is_invalid___d_>__ffff0000be0aac56,
               s_mrdump_get_reserved_info_by_inde_ffff0000be0c364d,0x82,param_1,0x80);
  }
  return 0xffffffff;
}


