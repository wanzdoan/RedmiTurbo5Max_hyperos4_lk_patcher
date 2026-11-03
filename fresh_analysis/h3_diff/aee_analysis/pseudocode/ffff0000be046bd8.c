
long FUN_ffff0000be046bd8(long *param_1,undefined8 param_2)

{
  long lVar1;
  undefined4 uVar2;
  long lVar3;
  undefined4 uVar4;
  undefined1 auStack_9c8 [12];
  undefined4 local_9bc;
  undefined1 auStack_6e8 [1608];
  undefined4 local_a0;
  undefined4 local_9c;
  
  if (param_1 == (long *)0x0) {
    lVar1 = 0;
  }
  else {
    FUN_ffff0000be041b94();
    lVar1 = FUN_ffff0000be04025c(3,0);
    if (lVar1 != 0) {
      FUN_ffff0000be060ae0(auStack_9c8,0,0x998);
      if (*(int *)*param_1 == 2) {
        uVar4 = 0x2b;
        if ((int)param_1[2] == 2) {
          uVar4 = 0x2c;
        }
        uVar2 = 0x2d;
        if ((int)param_1[2] != 3) {
          uVar2 = uVar4;
        }
      }
      else {
        uVar2 = 0x102;
      }
      FUN_ffff0000be040870(lVar1,uVar2);
      FUN_ffff0000be041824(lVar1,param_1[1],param_2);
      if (*(code **)(param_1[1] + 0x50) != (code *)0x0) {
        (**(code **)(param_1[1] + 0x50))(param_2);
      }
      FUN_ffff0000be040950(lVar1,0,param_2,0);
      FUN_ffff0000be060ae0(auStack_9c8,0,0x998);
      FUN_ffff0000be0607b0(auStack_6e8,*param_1 + 0x170,0x5b0);
      lVar3 = *param_1;
      local_a0 = *(undefined4 *)(lVar3 + 0x18);
      local_9bc = 1;
      local_9c = *(undefined4 *)(lVar3 + 0x1c);
      FUN_ffff0000be0607b0(auStack_6e8,lVar3 + 0x170,0x5b0);
      FUN_ffff0000be04125c(lVar1,auStack_9c8,0);
    }
  }
  return lVar1;
}


