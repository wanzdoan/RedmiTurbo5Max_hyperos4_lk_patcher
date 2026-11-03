
void FUN_ffff0000be050c30(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  undefined4 uVar3;
  
  iVar1 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0a8df2,3);
  if (iVar1 == 0) {
    uVar3 = 1;
    pcVar2 = s__FASTBOOT__oem_p2u_on__ffff0000be0c473c;
  }
  else {
    iVar1 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0b8640,4);
    if (iVar1 != 0) {
      return;
    }
    uVar3 = 0;
    pcVar2 = s__FASTBOOT__oem_p2u_off__ffff0000be0b6d71;
  }
  FUN_ffff0000be05fd98(pcVar2);
  FUN_ffff0000be0248f4(0,uVar3);
  FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
  return;
}


