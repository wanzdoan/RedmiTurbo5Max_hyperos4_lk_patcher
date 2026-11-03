
int FUN_ffff0000b80684f4(long param_1,uint param_2,undefined8 param_3,long param_4)

{
  int iVar1;
  undefined8 local_68;
  long lStack_60;
  ulong local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  long lStack_40;
  undefined8 local_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  if (param_1 == 0) {
    iVar1 = 2;
  }
  else if (param_4 == 0) {
    iVar1 = 3;
  }
  else if (param_2 == 0) {
    iVar1 = 1;
  }
  else {
    local_58 = (ulong)param_2;
    uStack_28 = 0;
    uStack_50 = 0;
    local_48 = 0;
    local_68 = 0xffff000018005000;
    local_38 = 0x10;
    lStack_60 = param_1;
    lStack_40 = param_4;
    local_30 = param_3;
    FUN_ffff0000b8074ff4(0xffff000010000000,3);
    iVar1 = FUN_ffff0000b8073a44(&local_68);
    if (iVar1 == 0x72d1) {
      FUN_ffff0000b8073480(0xffff000018005000);
      FUN_ffff0000b8073510(0xffff000018005000);
    }
    FUN_ffff0000b8074ff4(0xffff000010000000,0);
  }
  return iVar1;
}


