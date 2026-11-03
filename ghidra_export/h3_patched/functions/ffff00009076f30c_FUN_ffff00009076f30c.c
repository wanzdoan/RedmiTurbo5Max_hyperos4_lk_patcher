// ffff00009076f30c  size=184  FUN_ffff00009076f30c

undefined4
FUN_ffff00009076f30c
          (undefined8 param_1,undefined4 param_2,undefined8 param_3,undefined8 param_4,int param_5)

{
  long lVar1;
  undefined8 uVar2;
  undefined4 local_24;
  
  lVar1 = FUN_ffff00009076dcd4();
  local_24 = 0;
  if (lVar1 != 0) {
    if (param_5 == 0) {
      uVar2 = FUN_ffff00009077ef88(param_3);
      local_24 = 0;
      lVar1 = FUN_ffff00009076db78(param_1,param_2,param_3,uVar2,&local_24);
      if (lVar1 != 0) {
        FUN_ffff00009077e858(lVar1,param_4,0);
        local_24 = 0;
      }
    }
    else {
      local_24 = 0xfffffffd;
    }
  }
  return local_24;
}


