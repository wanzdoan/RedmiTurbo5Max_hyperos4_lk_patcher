
int FUN_ffff0000be073ca0(undefined4 param_1,uint param_2,long param_3,uint param_4,uint *param_5)

{
  long lVar1;
  ushort uVar2;
  uint uVar3;
  undefined1 uVar4;
  undefined4 uVar5;
  undefined *puVar6;
  int iVar7;
  long lVar8;
  ulong uVar9;
  char *pcVar10;
  uint uVar11;
  long local_a0;
  uint local_98;
  undefined4 local_94;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  uint6 local_78;
  undefined2 uStack_72;
  undefined4 local_70;
  undefined4 uStack_6c;
  undefined *local_68;
  
  local_98 = 0;
  local_68 = &DAT_ffff0000be1b4c10;
  lVar8 = FUN_ffff0000be070acc(0);
  if ((param_4 & 0xff) == 0) {
    _local_78 = 0;
    local_70 = 0;
    uStack_6c = 0;
    local_88 = 0;
    local_80 = 0;
    local_90 = 0;
    local_94 = 0;
    FUN_ffff0000be01a730(lVar8 + 0x270,0xffffffff);
    iVar7 = FUN_ffff0000be072f38();
    if (iVar7 == 0) {
      iVar7 = FUN_ffff0000be072afc(param_1,0,&DAT_ffff0000be1b4e40,0x10,&local_98);
      if (iVar7 == 0) {
        uVar9 = FUN_ffff0000be06da88(lVar8,&local_94);
        if ((uVar9 & 1) == 0) {
          iVar7 = -6;
        }
        else {
          if (0xff < param_4) {
            uVar11 = param_4 >> 8;
            lVar1 = (long)&local_88 + 5;
            if (uVar11 < 2) {
              uVar11 = 1;
            }
            uVar9 = (ulong)uVar11;
            local_a0 = param_3;
            do {
              uVar5 = local_94;
              uVar11 = param_4;
              if (0xff < param_4) {
                uVar11 = 0x100;
              }
              FUN_ffff0000be060ae0(lVar1,0,0x10);
              puVar6 = local_68;
              uVar3 = local_98;
              local_90 = CONCAT44(uVar5,0xc4);
              uVar4 = (undefined1)param_1;
              local_78 = (uint6)_local_78 & 0xffffffffff00;
              local_70 = 0x200;
              _local_78 = CONCAT26(0xc,local_78);
              local_88 = CONCAT17(uVar4,0xecb50000000001);
              local_80 = 0x20000000001;
              if (param_3 == 0) {
                pcVar10 = s___s___RPMB_data_pointer_NULL_ffff0000be0b2836;
LAB_ffff0000be074020:
                FUN_ffff0000be05fd98(pcVar10,s_ufshcd_rpmb_write_data_req_prepa_ffff0000be0b3825);
                iVar7 = -5;
LAB_ffff0000be074050:
                pcVar10 = s___s___Prepare_frame_fail__ret__d_ffff0000be0bee5e;
LAB_ffff0000be07409c:
                FUN_ffff0000be05fd98(pcVar10,s_ufs_rpmb_write_ffff0000be0a3e7b,iVar7);
                iVar7 = -6;
                goto LAB_ffff0000be0740a4;
              }
              if (local_68 == (undefined *)0x0) {
                pcVar10 = s___s___RPMB_pfrm_pointer_NULL_ffff0000be0ad049;
                goto LAB_ffff0000be074020;
              }
              FUN_ffff0000be060ae0(local_68,0,0x200);
              FUN_ffff0000be0607b0(puVar6 + 0xe4,local_a0,uVar11);
              uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
              *(uint *)(puVar6 + 500) = uVar3 >> 0x10 | uVar3 << 0x10;
              *(undefined2 *)(puVar6 + 0x1fa) = 0x100;
              *(ushort *)(puVar6 + 0x1f8) =
                   (ushort)(param_2 >> 8) & 0xff | (ushort)((param_2 & 0xff00ff) << 8);
              *(undefined2 *)(puVar6 + 0x1fe) = 0x300;
              iVar7 = FUN_ffff0000be074520(puVar6);
              if (iVar7 != 0) {
                FUN_ffff0000be05fd98
                          (s___s___rpmb_get_mac_fail__ret__d_ffff0000be0c5ac6,
                           s_ufshcd_rpmb_write_data_req_prepa_ffff0000be0b3825,iVar7);
                goto LAB_ffff0000be074050;
              }
              FUN_ffff0000be0607b0(puVar6 + 0xc4,DAT_ffff0000be1b4c08,0x20);
              iVar7 = FUN_ffff0000be06eb3c(lVar8,&local_90);
              uVar5 = local_94;
              if (iVar7 != 0) {
                pcVar10 = s___s___Send_write_frame_fail__ret_ffff0000be0a77d8;
                goto LAB_ffff0000be07409c;
              }
              FUN_ffff0000be060ae0(lVar1,0,0x10);
              puVar6 = local_68;
              local_90 = CONCAT44(uVar5,0xc4);
              local_78 = (uint6)_local_78 & 0xffffffffff00;
              local_70 = 0x200;
              _local_78 = CONCAT26(0xc,local_78);
              local_88 = CONCAT17(uVar4,0xecb50000000001);
              local_80 = 0x20000000001;
              if (local_68 == (undefined *)0x0) {
                FUN_ffff0000be05fd98
                          (s___s___RPMB_pfrm_pointer_NULL_ffff0000be0ad049,
                           s_ufshcd_authen_result_read_req_pr_ffff0000be0bee80);
              }
              else {
                FUN_ffff0000be060ae0(local_68,0,0x200);
                *(undefined2 *)(puVar6 + 0x1fe) = 0x500;
              }
              iVar7 = FUN_ffff0000be06eb3c(lVar8,&local_90);
              uVar5 = local_94;
              if (iVar7 != 0) {
                pcVar10 = s___s___Sending_result_read_reques_ffff0000be0a9b19;
                goto LAB_ffff0000be07409c;
              }
              FUN_ffff0000be060ae0(lVar1,0,0x10);
              local_90 = CONCAT44(uVar5,0xc4);
              local_78 = (uint6)_local_78 & 0xffffffffff00;
              local_70 = 0x200;
              _local_78 = CONCAT26(0xc,local_78);
              local_88 = CONCAT17(uVar4,0xeca20000000002);
              local_80 = 0x20000000001;
              iVar7 = FUN_ffff0000be06eb3c(lVar8,&local_90);
              if (iVar7 != 0) {
                pcVar10 = s___s___Get_result_response_fail__r_ffff0000be0b701f;
                goto LAB_ffff0000be07409c;
              }
              if ((param_5 != (uint *)0x0) &&
                 (uVar2 = *(ushort *)(local_68 + 0x1fc),
                 *param_5 = (uint)(uVar2 >> 8) | (uVar2 & 0xff00ff) << 8, uVar2 != 0)) {
                FUN_ffff0000be05fd98
                          (s___s___result____d_ffff0000be0bee4b,s_ufs_rpmb_write_ffff0000be0a3e7b);
                iVar7 = -2;
                goto LAB_ffff0000be0740a4;
              }
              iVar7 = FUN_ffff0000be07411c(&local_90,local_98,0x300);
              if (iVar7 != 0) {
                FUN_ffff0000be05fd98
                          (s___s___Response_frame_check_Check_ffff0000be0a77fd,
                           s_ufs_rpmb_write_ffff0000be0a3e7b,iVar7);
                goto LAB_ffff0000be0740a4;
              }
              param_2 = param_2 + 1;
              param_4 = param_4 - uVar11;
              local_98 = local_98 + 1;
              local_a0 = local_a0 + 0x100;
              uVar9 = uVar9 - 1;
            } while (uVar9 != 0);
          }
          iVar7 = 0;
LAB_ffff0000be0740a4:
          FUN_ffff0000be06db58(lVar8,local_94);
        }
      }
      else {
        FUN_ffff0000be05fd98
                  (s___s___RPMB_rpmb_authen_read_coun_ffff0000be0ba722,
                   s_ufs_rpmb_write_ffff0000be0a3e7b,iVar7);
        iVar7 = -6;
      }
    }
    FUN_ffff0000be01a7c8(lVar8 + 0x270);
  }
  else {
    FUN_ffff0000be05fd98
              (s___s___RPMB_read_write_need_256_y_ffff0000be0bee20,s_ufs_rpmb_write_ffff0000be0a3e7b
              );
    iVar7 = -6;
  }
  return iVar7;
}


