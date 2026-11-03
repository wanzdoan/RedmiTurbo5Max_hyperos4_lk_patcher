
int FUN_ffff00009079606c(undefined1 param_1,uint param_2,long param_3,ulong param_4,uint *param_5)

{
  ushort uVar1;
  undefined4 uVar2;
  undefined *puVar3;
  int iVar4;
  long lVar5;
  ulong uVar6;
  char *pcVar7;
  uint uVar8;
  undefined4 local_94;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  uint6 local_78;
  undefined2 uStack_72;
  undefined4 local_70;
  undefined4 uStack_6c;
  undefined *local_68;
  
  local_68 = &DAT_ffff00009090a590;
  lVar5 = FUN_ffff000090793148(0);
  if ((param_4 & 0xff) == 0) {
    _local_78 = 0;
    local_70 = 0;
    uStack_6c = 0;
    local_88 = 0;
    local_80 = 0;
    local_90 = 0;
    local_94 = 0;
    FUN_ffff000090717738(lVar5 + 0x270,0xffffffff);
    uVar6 = FUN_ffff00009078ff68(lVar5,&local_94);
    if ((uVar6 & 1) == 0) {
      iVar4 = -6;
    }
    else {
      FUN_ffff0000907957c4();
      if ((uint)param_4 < 0x100) {
        iVar4 = 0;
      }
      else {
        uVar8 = (uint)(param_4 >> 8) & 0xffffff;
        if (uVar8 < 2) {
          uVar8 = 1;
        }
        uVar6 = (ulong)uVar8;
        do {
          uVar2 = local_94;
          FUN_ffff00009077ee50((long)&local_88 + 5,0,0x10);
          puVar3 = local_68;
          local_80 = 0x20000000001;
          local_90 = CONCAT44(uVar2,0xc4);
          local_78 = (uint6)_local_78 & 0xffffffffff00;
          local_70 = 0x200;
          _local_78 = CONCAT26(0xc,local_78);
          local_88 = CONCAT17(param_1,0xecb50000000001);
          if (local_68 == (undefined *)0x0) {
            FUN_ffff00009077e11c
                      (s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd515,
                       s_ufshcd_rpmb_read_data_req_prepar_ffff0000907e3d04);
            pcVar7 = s___s___RPMB_Fail_in_prepare_frame_ffff0000907d6f55;
            iVar4 = -5;
LAB_ffff0000907962e8:
            FUN_ffff00009077e11c(pcVar7,s_ufs_rpmb_read_ffff0000907d3a56,iVar4);
            iVar4 = -6;
            goto LAB_ffff000090796330;
          }
          FUN_ffff00009077ee50(local_68,0,0x200);
          *(undefined2 *)(puVar3 + 0x1fa) = 0x100;
          *(ushort *)(puVar3 + 0x1f8) =
               (ushort)(param_2 >> 8) & 0xff | (ushort)((param_2 & 0xff00ff) << 8);
          *(undefined2 *)(puVar3 + 0x1fe) = 0x400;
          FUN_ffff00009077eb20(puVar3 + 0x1e4,&DAT_ffff00009090a7c0,0x10);
          iVar4 = FUN_ffff00009079101c(lVar5,&local_90);
          uVar2 = local_94;
          if (iVar4 != 0) {
            pcVar7 = s___s___RPMB_Fail_in_send_read_fra_ffff0000907f21d2;
            goto LAB_ffff0000907962e8;
          }
          FUN_ffff00009077ee50((long)&local_88 + 5,0,0x10);
          local_90 = CONCAT44(uVar2,0xc4);
          local_80 = 0x20000000001;
          local_78 = (uint6)_local_78 & 0xffffffffff00;
          local_70 = 0x200;
          _local_78 = CONCAT26(0xc,local_78);
          local_88 = CONCAT17(param_1,0xeca20000000002);
          iVar4 = FUN_ffff00009079101c(lVar5,&local_90);
          if (iVar4 != 0) {
            pcVar7 = s___s___RPMB_Fail_in_get_read_resp_ffff0000907f57c0;
            goto LAB_ffff0000907962e8;
          }
          if ((param_5 != (uint *)0x0) &&
             (uVar1 = *(ushort *)(local_68 + 0x1fc),
             *param_5 = (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8, uVar1 != 0)) {
            FUN_ffff00009077e11c
                      (s___s___result____d_ffff0000907f74af,s_ufs_rpmb_read_ffff0000907d3a56);
            iVar4 = -2;
            goto LAB_ffff000090796330;
          }
          iVar4 = FUN_ffff00009079636c(&local_90,&DAT_ffff00009090a7c0,param_3,0x400);
          if (iVar4 != 0) {
            FUN_ffff00009077e11c
                      (s___s___RPMB_Data_Read_Resp_Check_F_ffff0000907e6f4c,
                       s_ufs_rpmb_read_ffff0000907d3a56,iVar4);
            goto LAB_ffff000090796330;
          }
          param_2 = param_2 + 1;
          param_3 = param_3 + 0x100;
          uVar6 = uVar6 - 1;
        } while (uVar6 != 0);
        iVar4 = 0;
      }
LAB_ffff000090796330:
      FUN_ffff000090790038(lVar5,local_94);
    }
    FUN_ffff0000907177d0(lVar5 + 0x270);
  }
  else {
    FUN_ffff00009077e11c
              (s___s___RPMB_read_write_need_256_y_ffff0000907f7484,s_ufs_rpmb_read_ffff0000907d3a56)
    ;
    iVar4 = -6;
  }
  return iVar4;
}


