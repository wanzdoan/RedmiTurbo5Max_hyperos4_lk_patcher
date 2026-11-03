
uint FUN_ffff0000be0099a8(long *param_1,undefined1 *param_2,uint param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  ulong uVar4;
  char *pcVar5;
  undefined8 uVar6;
  uint uVar7;
  undefined1 *puVar8;
  
  param_1[1] = param_1[1] + (long)(int)param_3;
  if ((param_2 == (undefined1 *)0x0) && (param_3 == 0)) {
    uVar4 = (**(code **)(*param_1 + 0x20))(*param_1,param_1[2],(long)param_1 + 0x1c,(int)param_1[3])
    ;
    if ((uVar4 & 1) != 0) {
      return 0;
    }
    pcVar5 = s__s__d__SDCard__Write_dump_data_f_ffff0000be0c285a;
    uVar6 = 99;
  }
  else {
    if (param_2 == (undefined1 *)0x0) {
      return 0xffffffff;
    }
    uVar4 = (ulong)*(uint *)(param_1 + 3);
    uVar3 = param_3;
    while( true ) {
      uVar2 = 0x10000 - (int)uVar4;
      uVar7 = uVar2;
      if (uVar3 <= uVar2) {
        uVar7 = uVar3;
      }
      uVar1 = 0;
      if (uVar2 <= uVar3) {
        uVar1 = uVar3 - uVar2;
      }
      if (uVar7 != 0) break;
LAB_ffff0000be009a6c:
      if ((int)uVar4 == 0x10000) {
        uVar4 = (**(code **)(*param_1 + 0x20))(*param_1,param_1[2],(long)param_1 + 0x1c,0x10000);
        pcVar5 = s_SDCard__Write_dump_data_failed__ffff0000be0c17bd;
        if ((uVar4 & 1) == 0) goto LAB_ffff0000be009adc;
        uVar4 = 0;
        *(undefined4 *)(param_1 + 3) = 0;
        param_1[2] = param_1[2] + 0x10000;
      }
      if ((int)uVar1 < 0) goto LAB_ffff0000be009ae4;
      if (uVar3 <= uVar2) {
        return param_3;
      }
      param_2 = param_2 + (uVar3 - uVar1);
      uVar3 = uVar1;
    }
    pcVar5 = s_Error__handle_>midx_<_0_ffff0000be0a3083;
    if (-1 < (int)uVar4) {
      uVar4 = uVar4 & 0xffffffff;
      puVar8 = param_2;
      do {
        uVar7 = uVar7 - 1;
        *(undefined1 *)((long)param_1 + 0x1c + uVar4) = *puVar8;
        uVar4 = uVar4 + 1;
        *(int *)(param_1 + 3) = (int)uVar4;
        puVar8 = puVar8 + 1;
      } while (uVar7 != 0);
      goto LAB_ffff0000be009a6c;
    }
LAB_ffff0000be009adc:
    FUN_ffff0000be0115fc(pcVar5);
LAB_ffff0000be009ae4:
    pcVar5 = s__s__d__SDCard__Store_and_Write_f_ffff0000be0af3ce;
    uVar6 = 0x73;
  }
  FUN_ffff0000be05fd98(pcVar5,s_lba_write_cb_ffff0000be0bcc9c,uVar6);
  return 0xffffffff;
}


