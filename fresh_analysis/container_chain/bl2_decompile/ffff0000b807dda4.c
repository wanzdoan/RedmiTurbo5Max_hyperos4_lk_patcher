
int FUN_ffff0000b807dda4(long param_1,char *param_2,int param_3)

{
  int iVar1;
  long *plVar2;
  code *pcVar3;
  long lVar4;
  
  iVar1 = -6;
  if (((param_2 != (char *)0x0) && (param_3 == 0x70)) && (*param_2 == '1')) {
    if (param_1 == 0) {
      iVar1 = -2;
    }
    else {
      pcVar3 = *(code **)(param_1 + 0x40);
      *(undefined8 *)(param_1 + 0x30) = 0;
      if (pcVar3 == (code *)0x0) {
        pcVar3 = FUN_ffff0000b8082260;
        *(undefined8 *)(param_1 + 0x50) = 0;
        *(code **)(param_1 + 0x40) = FUN_ffff0000b8082260;
        lVar4 = *(long *)(param_1 + 0x48);
      }
      else {
        lVar4 = *(long *)(param_1 + 0x48);
      }
      if (lVar4 == 0) {
        *(undefined1 **)(param_1 + 0x48) = &LAB_ffff0000b8082270;
      }
      plVar2 = (long *)(*pcVar3)(*(undefined8 *)(param_1 + 0x50),1,0x1bf8);
      if (plVar2 == (long *)0x0) {
        iVar1 = -4;
      }
      else {
        *(long **)(param_1 + 0x38) = plVar2;
        *plVar2 = param_1;
        plVar2[9] = 0;
        *(undefined4 *)(plVar2 + 1) = 0x3f34;
        iVar1 = FUN_ffff0000b807dc2c(param_1,0xf);
        if (iVar1 != 0) {
          (**(code **)(param_1 + 0x48))(*(undefined8 *)(param_1 + 0x50),plVar2);
          *(undefined8 *)(param_1 + 0x38) = 0;
        }
      }
    }
  }
  return iVar1;
}


