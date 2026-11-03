
/* WARNING: Type propagation algorithm not settling */

uint FUN_ffff0000be06e500(long *param_1,long *param_2,ulong param_3)

{
  ushort uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  char *pcVar5;
  
  param_3 = param_3 & 0xffffffff;
  uVar3 = 1 << (ulong)(*(uint *)((long)param_2 + 0x3c) & 0x1f);
  if ((*(byte *)(param_1 + 0x55) >> 1 & 1) == 0) {
    lVar4 = param_3 + 1;
    do {
      if ((*(uint *)(*param_1 + 0x58) & uVar3) == 0) {
        uVar2 = 0;
        param_1[0x30] =
             param_1[0x30] & (long)~(1 << (ulong)(*(uint *)((long)param_2 + 0x3c) & 0x1f));
        goto LAB_ffff0000be06e5b0;
      }
      FUN_ffff0000be05ede4(1000);
      lVar4 = lVar4 + -1;
    } while (lVar4 != 0);
    uVar2 = 0;
  }
  else {
    FUN_ffff0000be019544(*(undefined4 *)((long)param_1 + 0x2ac));
    uVar2 = FUN_ffff0000be01a590(&DAT_ffff0000be1b4900,param_3);
    FUN_ffff0000be0194d8(*(undefined4 *)((long)param_1 + 0x2ac));
  }
LAB_ffff0000be06e5b0:
  param_1[0x10] = param_1[0x10] & (long)~(1 << (ulong)(*(uint *)((long)param_2 + 0x3c) & 0x1f));
  if ((*(uint *)(*param_1 + 0x58) & uVar3) != 0) {
    FUN_ffff0000be05fd98(s__UFS___d_ms_timedout_waiting_for_ffff0000be0a8897,param_3);
    uVar2 = 0xfffffffd;
  }
  uVar3 = DAT_ffff0000be1b4930;
  if ((*(byte *)(param_1 + 0x55) >> 1 & 1) == 0) {
    uVar3 = *(uint *)(*param_1 + 0x20);
  }
  if ((uVar3 & 0x30804) != 0) {
    FUN_ffff0000be05fd98(s__UFS__ERR__intr_status__0x_x_ffff0000be0a3d38,uVar3);
    if ((uVar3 >> 2 & 1) != 0) {
      uVar3 = *(uint *)(*param_1 + 0x38);
      if (uVar3 != 0) {
        FUN_ffff0000be05fd98(s__UFS__Host_UIC_Error_Code_PHY_Ad_ffff0000be0b6eea,uVar3);
        if ((uVar3 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__PHY_error_on_Lane_0_ffff0000be0bede5);
        }
        if ((uVar3 >> 1 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__PHY_error_on_Lane_1_ffff0000be0bb670);
        }
        if ((uVar3 >> 2 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__PHY_error_on_Lane_2_ffff0000be0c4907);
        }
        if ((uVar3 >> 3 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__PHY_error_on_Lane_3_ffff0000be0b6f1d);
        }
        if ((uVar3 >> 4 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__Generic_PHY_Adapter_Error__ffff0000be0b3713);
          FUN_ffff0000be05fd98(s__UFS__This_should_be_the_LINERES_ffff0000be0b5cdb);
        }
      }
      uVar3 = *(uint *)(*param_1 + 0x3c);
      if (uVar3 != 0) {
        FUN_ffff0000be05fd98(s__UFS__Host_UIC_Error_Code_Data_L_ffff0000be0c6ae9,uVar3);
        if ((uVar3 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__NAC_RECEIVED_ffff0000be0bb68b);
        }
        if ((uVar3 >> 1 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__TCx_REPLAY_TIMER_EXPIRED_ffff0000be0b80ed);
        }
        if ((uVar3 >> 2 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__AFCx_REQUEST_TIMER_EXPIRED_ffff0000be0c4922);
        }
        if ((uVar3 >> 3 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__FCx_PROTECTION_TIMER_EXPIR_ffff0000be0c372f);
        }
        if ((uVar3 >> 4 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__CRC_ERROR_ffff0000be0bb69f);
        }
        if ((uVar3 >> 5 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__RX_BUFFER_OVERFLOW_ffff0000be0b3735);
        }
        if ((uVar3 >> 6 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__MAX_FRAME_LENGTH_EXCEEDEDn_ffff0000be0bdae9);
        }
        if ((uVar3 >> 7 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__WRONG_SEQUENCE_NUMBER_ffff0000be0adf85);
        }
        if ((uVar3 >> 8 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__AFC_FRAME_SYNTAX_ERROR_ffff0000be0c6b1a);
        }
        if ((uVar3 >> 9 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__NAC_FRAME_SYNTAX_ERROR_ffff0000be0c3753);
        }
        if ((uVar3 >> 10 & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__EOF_SYNTAX_ERROR_ffff0000be0b27b7);
        }
        if ((uVar3 >> 0xb & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__FRAME_SYNTAX_ERROR_ffff0000be0adfa2);
        }
        if ((uVar3 >> 0xc & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__BAD_CTRL_SYMBOL_TYPE_ffff0000be0c138a);
        }
        if ((uVar3 >> 0xd & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__PA_INIT_ERROR__FATAL_ERROR_ffff0000be0b0583);
        }
        if ((uVar3 >> 0xe & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__PA_ERROR_IND_RECEIVED_ffff0000be0b4a6d);
        }
        if ((uVar3 >> 0xf & 1) != 0) {
          FUN_ffff0000be05fd98(s__UFS__PA_INIT__3_0_FATAL_ERROR__ffff0000be0aada1);
        }
      }
      FUN_ffff0000be05fd98
                (s__UFS__Host_UIC_Error_Code_Networ_ffff0000be0ba63b,
                 *(undefined4 *)(*param_1 + 0x40));
      FUN_ffff0000be05fd98
                (s__UFS__Host_UIC_Error_Code_Transp_ffff0000be0c3771,
                 *(undefined4 *)(*param_1 + 0x44));
      FUN_ffff0000be05fd98
                (s__UFS__Host_UIC_Error_Code__0x_x_ffff0000be0c255a,*(undefined4 *)(*param_1 + 0x48)
                );
    }
    *(undefined4 *)(*param_1 + 0x20) = 0x30804;
  }
  if (uVar2 == 0xfffffffd) {
    FUN_ffff0000be074948(param_1);
  }
  else {
    lVar4 = param_2[4];
    if ((((lVar4 != 0) && (*(long *)(lVar4 + 0x28) != 0)) && (*(int *)(lVar4 + 0x20) != 0)) &&
       (*(int *)(lVar4 + 8) != 1)) {
      FUN_ffff0000be007348();
    }
    FUN_ffff0000be007348(*param_2,0x20);
    if ((*(uint *)(*param_2 + 8) & 0xf) == 0) {
      if (param_2[2] != 0) {
        FUN_ffff0000be007348(param_2[2],0x34);
      }
      pcVar5 = (char *)param_2[2];
      if ((int)param_2[7] == 2) {
        if (*pcVar5 == ' ') {
          return -(uint)((int)param_1[0x37] != 0);
        }
        if (*pcVar5 == '6') {
          uVar1 = *(ushort *)(pcVar5 + 6);
          *(char *)(param_1 + 0x3c) = (char)uVar1;
          if (0xff < ((uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8)) {
            return uVar1 & 0xff;
          }
          FUN_ffff0000be0607b0((long)param_1 + 0x1e4,pcVar5 + 0xc,0x14);
          lVar4 = param_2[2];
          if (*(char *)(lVar4 + 0xc) != '\x01') {
            return 0;
          }
          uVar2 = (uint)*(ushort *)(lVar4 + 10);
          uVar3 = ((uVar2 & 0xff00) << 8 | uVar2 << 0x18) >> 0x10;
          if (uVar3 <= ((uint)(*(ushort *)((long)param_1 + 0x1ca) >> 8) |
                       (*(ushort *)((long)param_1 + 0x1ca) & 0xff00ff) << 8)) {
            if (uVar2 != 0) {
              FUN_ffff0000be007348(lVar4 + 0x20,uVar3);
            }
            FUN_ffff0000be0607b0(param_1[0x3b],lVar4 + 0x20,uVar3);
            return 0;
          }
        }
        return 0xffffffff;
      }
      uVar1 = *(ushort *)(pcVar5 + 6);
      if (0xff < (ushort)(uVar1 >> 8 | uVar1 << 8)) {
        if (*pcVar5 == '!') {
          FUN_ffff0000be074798(param_1,pcVar5[0x2e],pcVar5[0x2f]);
          FUN_ffff0000be05fd98(s__UFS__Sense_Data_all___ffff0000be0ba5eb);
          lVar4 = param_2[2];
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x22));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x23));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x24));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x25));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x26));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x27));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x28));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x29));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x2a));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x2b));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x2c));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x2d));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x2e));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x2f));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x30));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x31));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x32));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(lVar4 + 0x33));
          FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
        }
        if ((*(byte *)(param_2[2] + 0x24) & 0xf) == 6) {
          uVar2 = 0xfffffff8;
        }
        else {
          FUN_ffff0000be05fd98(s__UFS__err__task_response_error___ffff0000be0c5a6a,uVar1 & 0xff);
          uVar2 = 0xfffffffb;
        }
      }
    }
    else {
      FUN_ffff0000be05fd98
                (s__UFS__err__OCS_error____x__T__d_ffff0000be0c6a3b,*(uint *)(*param_2 + 8) & 0xf,
                 *(undefined4 *)((long)param_2 + 0x3c));
      uVar2 = 0xfffffffc;
    }
  }
  if (param_2[4] != 0) {
    param_2[4] = 0;
  }
  return uVar2;
}


