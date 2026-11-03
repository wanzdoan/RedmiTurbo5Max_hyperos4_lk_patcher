
int FUN_ffff0000be01269c(long *param_1,int param_2)

{
  undefined4 uVar1;
  int iVar2;
  ulong uVar3;
  char *pcVar4;
  undefined8 uVar5;
  
  if ((int)param_1[4] == 0x3fe) {
    *(uint *)((long)param_1 + 0x1c) = *(uint *)((long)param_1 + 0x1024);
    uVar3 = (**(code **)(*param_1 + 0x18))
                      (*param_1,(ulong)*(uint *)((long)param_1 + 0x1024) << 0xc,(long)param_1 + 0x2c
                       ,0x1000);
    if ((uVar3 & 1) == 0) {
      pcVar4 = s__s__d__SDCard__Reading_BlockLBA_f_ffff0000be0bbbde;
      uVar5 = 0x7c;
    }
    else {
      uVar1 = FUN_ffff0000be0553a4(0,0,0);
      iVar2 = FUN_ffff0000be0553a4(uVar1,(long)param_1 + 0x2c,0xffc);
      param_2 = 0;
      if ((int)param_1[0x205] == iVar2) goto LAB_ffff0000be012734;
      pcVar4 = s__s__d__Get_next_index_crc32_erro_ffff0000be0ad343;
      uVar5 = 0x83;
    }
    FUN_ffff0000be05fd98(pcVar4,s_Get_Next_bidx_ffff0000be0bce0a,uVar5);
    param_2 = -1;
  }
  else {
    param_2 = (int)param_1[4] + param_2;
LAB_ffff0000be012734:
    *(int *)(param_1 + 4) = param_2;
  }
  return param_2;
}


