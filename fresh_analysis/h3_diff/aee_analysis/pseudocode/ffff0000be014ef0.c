
undefined8
FUN_ffff0000be014ef0
          (int *param_1,undefined4 *param_2,undefined8 param_3,undefined8 param_4,
          undefined8 *param_5,long param_6,int *param_7,uint *param_8)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  undefined4 uVar5;
  
  iVar2 = *param_7;
  DAT_ffff0000be107a34 = *param_8;
  DAT_ffff0000be107a30 = iVar2;
  *param_1 = (uint)(DAT_ffff0000be107a34 == 0) << 2;
  FUN_ffff0000be05fd98(s____Compress_expdb_offset__llx__s_ffff0000be0ac45a,iVar2);
  do {
    uVar1 = DAT_ffff0000be107a34;
    if (0x7fff < DAT_ffff0000be107a34) {
      uVar1 = 0x8000;
    }
    FUN_ffff0000be014e28(param_3,uVar1,DAT_ffff0000be107a30);
    uVar1 = DAT_ffff0000be107a34;
    if (0x7fff < DAT_ffff0000be107a34) {
      uVar1 = 0x8000;
    }
    uVar4 = FUN_ffff0000be0553a4(~DAT_ffff0000be0d6098,param_3,uVar1);
    DAT_ffff0000be0d6098 = ~uVar4;
    *(uint *)(param_5 + 1) = uVar1;
    DAT_ffff0000be107a34 = DAT_ffff0000be107a34 - uVar1;
    *param_5 = param_3;
    do {
      *(undefined4 *)(param_5 + 4) = 0x8000;
      iVar2 = *param_1;
      param_5[3] = param_4;
      uVar5 = FUN_ffff0000be057f18(param_5,iVar2);
      *param_2 = uVar5;
      iVar2 = *(int *)(param_5 + 4);
      iVar3 = 0x8000 - iVar2;
      if (0 < iVar3) {
        FUN_ffff0000be0607b0(param_6 + (ulong)DAT_ffff0000be107a2c,param_4,iVar3);
        DAT_ffff0000be107a2c = DAT_ffff0000be107a2c + iVar3;
        iVar2 = *(int *)(param_5 + 4);
      }
      DAT_ffff0000be107a38 = (undefined4)param_5[5];
    } while (iVar2 == 0);
    DAT_ffff0000be107a30 = DAT_ffff0000be107a30 + uVar1;
  } while (DAT_ffff0000be107a34 != 0);
  FUN_ffff0000be05fd98(s____Compress_expdb_offset__llx__d_ffff0000be0b2ae1,DAT_ffff0000be107a30,0);
  *param_7 = DAT_ffff0000be107a30;
  *param_8 = DAT_ffff0000be107a34;
  return 1;
}


