
undefined4 FUN_ffff0000b80611e0(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  long lVar2;
  long lVar3;
  long lVar4;
  long lVar5;
  undefined8 in_x7;
  long lVar6;
  undefined4 uVar7;
  undefined8 unaff_x29;
  undefined8 unaff_x30;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 1) {
    return 0;
  }
  lVar2 = FUN_ffff0000b804bccc(0x180);
  if (lVar2 == 0) {
    uVar7 = 0xffffffff;
  }
  else {
    lVar3 = FUN_ffff0000b804bccc(0x300);
    if (lVar3 == 0) {
      uVar7 = 0xfffffffe;
    }
    else {
      lVar4 = FUN_ffff0000b804bccc(0x300);
      if (lVar4 == 0) {
        uVar7 = 0xfffffffd;
      }
      else {
        lVar5 = FUN_ffff0000b804bccc(0x300);
        if (lVar5 == 0) {
          uVar7 = 0xfffffffc;
          lVar6 = lVar2;
          lVar2 = lVar3;
          lVar3 = lVar4;
        }
        else {
          FUN_ffff0000b8060660
                    (lVar2,param_1,param_1,param_2,lVar3,lVar4,lVar5,in_x7,unaff_x29,unaff_x30);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,lVar2,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b8060660(lVar2,lVar2,param_1,param_2,lVar3,lVar4,lVar5);
          FUN_ffff0000b804cd40(param_3,lVar2,0x180);
          thunk_FUN_ffff0000b8082558(lVar2);
          uVar7 = 0;
          lVar6 = lVar3;
          lVar2 = lVar4;
          lVar3 = lVar5;
        }
        thunk_FUN_ffff0000b8082558(lVar6);
      }
      thunk_FUN_ffff0000b8082558(lVar2);
      lVar2 = lVar3;
    }
    thunk_FUN_ffff0000b8082558(lVar2);
  }
  return uVar7;
}


