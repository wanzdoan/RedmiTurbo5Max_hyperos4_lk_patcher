// ffff0000907aaf90  size=164  FUN_ffff0000907aaf90

undefined4 FUN_ffff0000907aaf90(long param_1,uint param_2,long param_3,long param_4,long param_5)

{
  undefined4 uVar1;
  undefined8 local_68;
  long lStack_60;
  ulong local_58;
  long lStack_50;
  undefined8 local_48;
  long lStack_40;
  undefined8 local_38;
  long local_30;
  undefined8 uStack_28;
  
  uVar1 = 2;
  if (((param_1 != 0) && (param_3 != 0)) && (param_4 != 0)) {
    if (param_5 == 0) {
      uVar1 = 3;
    }
    else {
      uVar1 = 1;
      if (param_2 != 0) {
        local_58 = (ulong)param_2;
        local_48 = 0;
        local_68 = 0xffff000018005000;
        local_38 = 0;
        uStack_28 = 0x100000000;
        lStack_60 = param_1;
        lStack_50 = param_4;
        lStack_40 = param_5;
        local_30 = param_3;
        FUN_ffff0000907b9e2c(&DAT_ffff000010000000,3);
        uVar1 = FUN_ffff0000907b8450(&local_68);
        FUN_ffff0000907b9e2c(&DAT_ffff000010000000,0);
      }
    }
  }
  return uVar1;
}


