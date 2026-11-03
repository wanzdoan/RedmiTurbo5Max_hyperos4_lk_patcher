// ffff00009079626c  size=1148  FUN_ffff00009079626c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009079626c(undefined4 param_1,uint param_2,long param_3,uint param_4,uint *param_5)

{
  long lVar1;
  ushort uVar2;
  uint uVar3;
  undefined1 uVar4;
  undefined4 uVar5;
  long lVar6;
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
  long local_68;
  
  local_98 = 0;
  local_68 = -0xffff6f6f5b30;
  lVar8 = FUN_ffff000090792e88(0);
  if ((param_4 & 0xff) == 0) {
    _local_78 = 0;
    local_70 = 0;
    uStack_6c = 0;
    local_88 = 0;
    local_80 = 0;
    local_90 = 0;
    local_94 = 0;
    FUN_ffff0000907175f4(lVar8 + 0x270,0xffffffff);
    iVar7 = FUN_ffff000090795504();
    if (iVar7 == 0) {
      iVar7 = FUN_ffff0000907950c8(param_1,0,0xffff00009090a700,0x10,&local_98);
      if (iVar7 == 0) {
        uVar9 = FUN_ffff00009078fca8(lVar8,&local_94);
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
              FUN_ffff00009077eb88(lVar1,0,0x10);
              lVar6 = local_68;
              uVar3 = local_98;
              local_90 = CONCAT44(uVar5,0xc4);
              uVar4 = (undefined1)param_1;
              local_78 = (uint6)_local_78 & 0xffffffffff00;
              local_70 = 0x200;
              _local_78 = CONCAT26(0xc,local_78);
              local_88 = CONCAT17(uVar4,0xecb50000000001);
              local_80 = 0x20000000001;
              if (param_3 == 0) {
                pcVar10 = s___s___RPMB_data_pointer_NULL_ffff0000907e5619;
LAB_ffff0000907965ec:
                FUN_ffff00009077de54(pcVar10,s_ufshcd_rpmb_write_data_req_prepa_ffff0000907e6e6d);
                iVar7 = -5;
LAB_ffff00009079661c:
                pcVar10 = s___s___Prepare_frame_fail__ret__d_ffff0000907f7338;
LAB_ffff000090796668:
                FUN_ffff00009077de54(pcVar10,s_ufs_rpmb_write_ffff0000907d051d,iVar7);
                iVar7 = -6;
                goto LAB_ffff000090796670;
              }
              if (local_68 == 0) {
                pcVar10 = s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd45b;
                goto LAB_ffff0000907965ec;
              }
              FUN_ffff00009077eb88(local_68,0,0x200);
              FUN_ffff00009077e858(lVar6 + 0xe4,local_a0,uVar11);
              uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
              *(uint *)(lVar6 + 500) = uVar3 >> 0x10 | uVar3 << 0x10;
              *(undefined2 *)(lVar6 + 0x1fa) = 0x100;
              *(ushort *)(lVar6 + 0x1f8) =
                   (ushort)(param_2 >> 8) & 0xff | (ushort)((param_2 & 0xff00ff) << 8);
              *(undefined2 *)(lVar6 + 0x1fe) = 0x300;
              iVar7 = FUN_ffff000090796aec(lVar6);
              if (iVar7 != 0) {
                FUN_ffff00009077de54
                          (s___s___rpmb_get_mac_fail__ret__d_ffff00009080179c,
                           s_ufshcd_rpmb_write_data_req_prepa_ffff0000907e6e6d,iVar7);
                goto LAB_ffff00009079661c;
              }
              FUN_ffff00009077e858(lVar6 + 0xc4,_DAT_ffff00009090a4c8,0x20);
              iVar7 = FUN_ffff000090790d5c(lVar8,&local_90);
              uVar5 = local_94;
              if (iVar7 != 0) {
                pcVar10 = s___s___Send_write_frame_fail__ret_ffff0000907d52e2;
                goto LAB_ffff000090796668;
              }
              FUN_ffff00009077eb88(lVar1,0,0x10);
              lVar6 = local_68;
              local_90 = CONCAT44(uVar5,0xc4);
              local_78 = (uint6)_local_78 & 0xffffffffff00;
              local_70 = 0x200;
              _local_78 = CONCAT26(0xc,local_78);
              local_88 = CONCAT17(uVar4,0xecb50000000001);
              local_80 = 0x20000000001;
              if (local_68 == 0) {
                FUN_ffff00009077de54
                          (s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd45b,
                           s_ufshcd_authen_result_read_req_pr_ffff0000907f735a);
              }
              else {
                FUN_ffff00009077eb88(local_68,0,0x200);
                *(undefined2 *)(lVar6 + 0x1fe) = 0x500;
              }
              iVar7 = FUN_ffff000090790d5c(lVar8,&local_90);
              uVar5 = local_94;
              if (iVar7 != 0) {
                pcVar10 = s___s___Sending_result_read_reques_ffff0000907d8739;
                goto LAB_ffff000090796668;
              }
              FUN_ffff00009077eb88(lVar1,0,0x10);
              local_90 = CONCAT44(uVar5,0xc4);
              local_78 = (uint6)_local_78 & 0xffffffffff00;
              local_70 = 0x200;
              _local_78 = CONCAT26(0xc,local_78);
              local_88 = CONCAT17(uVar4,0xeca20000000002);
              local_80 = 0x20000000001;
              iVar7 = FUN_ffff000090790d5c(lVar8,&local_90);
              if (iVar7 != 0) {
                pcVar10 = s___s___Get_result_response_fail__r_ffff0000907eb98f;
                goto LAB_ffff000090796668;
              }
              if ((param_5 != (uint *)0x0) &&
                 (uVar2 = *(ushort *)(local_68 + 0x1fc),
                 *param_5 = (uint)(uVar2 >> 8) | (uVar2 & 0xff00ff) << 8, uVar2 != 0)) {
                FUN_ffff00009077de54
                          (s___s___result____d_ffff0000907f7325,s_ufs_rpmb_write_ffff0000907d051d);
                iVar7 = -2;
                goto LAB_ffff000090796670;
              }
              iVar7 = FUN_ffff0000907966e8(&local_90,local_98,0x300);
              if (iVar7 != 0) {
                FUN_ffff00009077de54
                          (s___s___Response_frame_check_Check_ffff0000907d5307,
                           s_ufs_rpmb_write_ffff0000907d051d,iVar7);
                goto LAB_ffff000090796670;
              }
              param_2 = param_2 + 1;
              param_4 = param_4 - uVar11;
              local_98 = local_98 + 1;
              local_a0 = local_a0 + 0x100;
              uVar9 = uVar9 - 1;
            } while (uVar9 != 0);
          }
          iVar7 = 0;
LAB_ffff000090796670:
          FUN_ffff00009078fd78(lVar8,local_94);
        }
      }
      else {
        FUN_ffff00009077de54
                  (s___s___RPMB_rpmb_authen_read_coun_ffff0000907f0739,
                   s_ufs_rpmb_write_ffff0000907d051d,iVar7);
        iVar7 = -6;
      }
    }
    FUN_ffff00009071768c(lVar8 + 0x270);
  }
  else {
    FUN_ffff00009077de54
              (s___s___RPMB_read_write_need_256_y_ffff0000907f72fa,s_ufs_rpmb_write_ffff0000907d051d
              );
    iVar7 = -6;
  }
  return iVar7;
}


