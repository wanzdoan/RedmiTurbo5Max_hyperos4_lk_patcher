
void FUN_ffff0000be02e5e4(long param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  long lVar4;
  ulong uVar5;
  int local_44;
  
  DAT_ffff0000be119498 = 0;
  if (param_1 != 0) {
    iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__tracker_ffff0000be0d72c8);
    if ((iVar2 < 0) && (iVar2 = FUN_ffff0000be05620c(param_1,DAT_ffff0000be0d72d0), iVar2 < 0)) {
      FUN_ffff0000be05fd98
                (s__s__couldn_t_find_the_tracker_no_ffff0000be0a5c2c,
                 s_get_tracker_setting_from_fdt_ffff0000be0c1c79);
      return;
    }
    local_44 = 0;
    puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_timer_sep_ffff0000be0be400,&local_44);
    if ((puVar3 == (uint *)0x0) || (local_44 == 0)) {
      FUN_ffff0000be05fd98
                (s__s__couldn_t_find_property_enabl_ffff0000be0be3b5,
                 s_get_tracker_setting_from_fdt_ffff0000be0c1c79);
      DAT_ffff0000be119560 = 0;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be119560 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_no_w_valid_chk_ffff0000be0baedd,&local_44)
    ;
    if ((puVar3 == (uint *)0x0) || (local_44 == 0)) {
      FUN_ffff0000be05fd98
                (s__s__couldn_t_find_property_no_w__ffff0000be0c1c96,
                 s_get_tracker_setting_from_fdt_ffff0000be0c1c79);
      DAT_ffff0000be119564 = 0;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      DAT_ffff0000be119564 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    iVar2 = FUN_ffff0000be055ffc(param_1,iVar2,s_subsys_ffff0000be0c2dcf);
    if (iVar2 < 0) {
      FUN_ffff0000be05fd98
                (s__s__couldn_t_find_the_subsys_sub_ffff0000be0afb08,
                 s_get_tracker_setting_from_fdt_ffff0000be0c1c79);
    }
    else {
      for (iVar2 = FUN_ffff0000be055944(param_1,iVar2); -1 < iVar2;
          iVar2 = FUN_ffff0000be055a14(param_1,iVar2)) {
        lVar4 = FUN_ffff0000be056494(param_1,iVar2,s_sys_name_ffff0000be0b9d8b,&local_44);
        uVar5 = (ulong)DAT_ffff0000be119498;
        if ((lVar4 == 0) || (local_44 == 0)) goto LAB_ffff0000be02e868;
        FUN_ffff0000be060eb8(&DAT_ffff0000be1194a0 + uVar5 * 0x30,lVar4,(long)local_44);
        puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_sys_base_ffff0000be0b417a,&local_44);
        if ((puVar3 == (uint *)0x0) || (local_44 == 0)) {
LAB_ffff0000be02e810:
          uVar5 = (ulong)DAT_ffff0000be119498;
LAB_ffff0000be02e868:
          FUN_ffff0000be05fd98
                    (s__s__couldn_t_find_property_track_ffff0000be0afb2e,
                     s_get_tracker_setting_from_fdt_ffff0000be0c1c79,uVar5);
          return;
        }
        uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        (&DAT_ffff0000be1194c0)[(ulong)DAT_ffff0000be119498 * 6] =
             (long)(int)(uVar1 >> 0x10 | uVar1 << 0x10) + -0x1000000000000;
        puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_entry_num_ffff0000be0a802e,&local_44);
        if ((puVar3 == (uint *)0x0) || (local_44 == 0)) goto LAB_ffff0000be02e810;
        uVar5 = (ulong)DAT_ffff0000be119498;
        DAT_ffff0000be119498 = DAT_ffff0000be119498 + 1;
        uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        (&DAT_ffff0000be1194c8)[uVar5 * 0xc] = uVar1 >> 0x10 | uVar1 << 0x10;
      }
    }
  }
  return;
}


