// ffff0000907a0b80  size=788  FUN_ffff0000907a0b80

undefined4 FUN_ffff0000907a0b80(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  long lVar1;
  long lVar2;
  long lVar3;
  long lVar4;
  long lVar5;
  undefined4 uVar6;
  
  lVar1 = FUN_ffff00009077d514(0x100);
  if (lVar1 == 0) {
    uVar6 = 0xffffffff;
  }
  else {
    lVar2 = FUN_ffff00009077d514(0x200);
    if (lVar2 == 0) {
      uVar6 = 0xfffffffe;
    }
    else {
      lVar3 = FUN_ffff00009077d514(0x200);
      if (lVar3 == 0) {
        uVar6 = 0xfffffffd;
      }
      else {
        lVar4 = FUN_ffff00009077d514(0x200);
        if (lVar4 == 0) {
          uVar6 = 0xfffffffc;
          lVar5 = lVar1;
          lVar1 = lVar2;
          lVar2 = lVar3;
        }
        else {
          FUN_ffff0000907a0e94(lVar1,param_1,param_1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,lVar1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff0000907a0e94(lVar1,lVar1,param_1,param_2,lVar2,lVar3,lVar4);
          FUN_ffff00009077eb20(param_3,lVar1,0x100);
          thunk_FUN_ffff0000907c314c(lVar1);
          uVar6 = 0;
          lVar5 = lVar2;
          lVar1 = lVar3;
          lVar2 = lVar4;
        }
        thunk_FUN_ffff0000907c314c(lVar5);
      }
      thunk_FUN_ffff0000907c314c(lVar1);
      lVar1 = lVar2;
    }
    thunk_FUN_ffff0000907c314c(lVar1);
  }
  return uVar6;
}


