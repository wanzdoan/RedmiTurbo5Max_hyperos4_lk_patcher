
ulong FUN_ffff0000be07523c(undefined8 param_1)

{
  int iVar1;
  ulong uVar2;
  long *plVar3;
  
  uVar2 = 0;
  plVar3 = &DAT_ffff0000be0f37f0;
  while (((((plVar3[-2] == 0 || (iVar1 = FUN_ffff0000be060cb8(param_1), iVar1 != 0)) &&
           ((plVar3[-1] == 0 || (iVar1 = FUN_ffff0000be060cb8(param_1), iVar1 != 0)))) &&
          ((*plVar3 == 0 || (iVar1 = FUN_ffff0000be060cb8(param_1), iVar1 != 0)))) &&
         ((plVar3[1] == 0 || (iVar1 = FUN_ffff0000be060cb8(param_1), iVar1 != 0))))) {
    uVar2 = uVar2 + 1;
    plVar3 = plVar3 + 6;
    if (uVar2 == 0x17) {
LAB_ffff0000be0752cc:
      uVar2 = 0;
LAB_ffff0000be0752d0:
      return uVar2 & 0xffffffff;
    }
  }
  if ((int)uVar2 != 0x17) goto LAB_ffff0000be0752d0;
  goto LAB_ffff0000be0752cc;
}


