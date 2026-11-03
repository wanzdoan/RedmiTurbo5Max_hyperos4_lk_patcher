
undefined8 FUN_ffff0000be00ac04(undefined1 *param_1,undefined4 param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  char *pcVar5;
  
  if (DAT_ffff0000be0d6048 == 0) {
    iVar1 = FUN_ffff0000be018ca4(param_1 + 0x40,param_2);
    if (-1 < iVar1) {
      return 1;
    }
    pcVar4 = s__s__send_DATA_error__ffff0000be0ae385;
    pcVar5 = s_usb_data_transfer_norts_ffff0000be0bccba;
  }
  else {
    FUN_ffff0000be060ae0(param_1,0,0x40);
    FUN_ffff0000be0611dc(param_1,0x40,&DAT_ffff0000be0ac42b,&DAT_ffff0000be0b8533);
    uVar3 = FUN_ffff0000be060e5c(param_1);
    iVar1 = FUN_ffff0000be018ca4(param_1,uVar3);
    if (iVar1 < 1) {
      pcVar4 = s__s__send_RTS_error__ffff0000be0a6906;
    }
    else {
      FUN_ffff0000be060ae0(param_1,0,0x40);
      iVar1 = FUN_ffff0000be018bb8(param_1,0x40);
      iVar2 = FUN_ffff0000be060e5c(&DAT_ffff0000be0ac42e);
      if ((iVar1 != iVar2) ||
         (iVar2 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0ac42e), iVar2 != 0)) {
        FUN_ffff0000be0115fc
                  (s__s__Not_Clear_to_Send_after_RTS__ffff0000be0a4247,
                   s_usb_data_transfer_rts_ffff0000be0b2ac4,param_1,iVar1,*param_1,param_1[1],
                   param_1[2],param_1[3]);
        return 0;
      }
      iVar1 = FUN_ffff0000be018ca4(param_1 + 0x40,param_2);
      if (0 < iVar1) {
        FUN_ffff0000be060ae0(param_1,0,0x40);
        iVar1 = FUN_ffff0000be018bb8(param_1,0x40);
        iVar2 = FUN_ffff0000be060e5c(&DAT_ffff0000be0c2883);
        if ((iVar1 == iVar2) &&
           (iVar2 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0c2883), iVar2 == 0)) {
          return 1;
        }
        FUN_ffff0000be0115fc
                  (s__s__failed_to_get_FIN_cmd<_p,_d>_ffff0000be0a79d5,
                   s_usb_data_transfer_rts_ffff0000be0b2ac4,param_1,iVar1,*param_1,param_1[1],
                   param_1[2],param_1[3]);
        return 0;
      }
      pcVar4 = s__s__send_DATA_error__ffff0000be0ae385;
    }
    pcVar5 = s_usb_data_transfer_rts_ffff0000be0b2ac4;
  }
  FUN_ffff0000be0115fc(pcVar4,pcVar5);
  return 0;
}


