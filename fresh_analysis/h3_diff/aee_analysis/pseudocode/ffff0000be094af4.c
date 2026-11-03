
int FUN_ffff0000be094af4
              (undefined8 param_1,uint param_2,long param_3,long param_4,long param_5,long param_6)

{
  long lVar1;
  int iVar2;
  undefined1 auStack_668 [4];
  undefined1 auStack_664 [512];
  undefined1 auStack_464 [512];
  undefined1 auStack_264 [512];
  int local_64;
  
  local_64 = 0xff;
  if (param_2 < 6) {
    iVar2 = 0x7247;
    if ((((param_3 != 0) && (param_4 != 0)) && (param_5 != 0)) && (param_6 != 0)) {
      if (param_2 < 4) {
        lVar1 = (ulong)param_2 * 0x80 + 0x80;
        FUN_ffff0000be060ae0(auStack_668,0,0x604);
        FUN_ffff0000be060ae0(auStack_668,0,0x604);
        FUN_ffff0000be0607b0(auStack_264,param_3,lVar1);
        FUN_ffff0000be0607b0(auStack_464,param_4,lVar1);
        FUN_ffff0000be0607b0(auStack_664,param_5,lVar1);
        local_64 = FUN_ffff0000be094958
                             (param_1,(ulong)param_2,auStack_264,auStack_264,auStack_464,auStack_664
                              ,5000);
        iVar2 = local_64;
        if (local_64 == 0) {
          FUN_ffff0000be0607b0(param_6,auStack_264,lVar1);
          iVar2 = 0;
        }
      }
      else {
        iVar2 = 0x7242;
      }
    }
  }
  else {
    iVar2 = 0x7246;
  }
  return iVar2;
}


