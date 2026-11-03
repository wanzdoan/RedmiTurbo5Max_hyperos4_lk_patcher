// ffff000090795064  size=236  FUN_ffff000090795064

undefined8 FUN_ffff000090795064(long param_1,undefined4 param_2,int *param_3)

{
  undefined8 uVar1;
  
  switch(param_2) {
  case 0x100:
    if ((param_3 != (int *)0x0) && (FUN_ffff000090793148(0), *param_3 - 1U < 2)) {
      uVar1 = FUN_ffff000090792c1c();
      return uVar1;
    }
  default:
    uVar1 = 0xffffffe8;
    break;
  case 0x101:
    FUN_ffff00009077e11c
              (s__UFS__d__lu__u__u_ffff0000907fe45b,*(undefined4 *)(param_1 + 0xa8),
               *(undefined8 *)param_3,param_3[2],(char)param_3[3]);
    FUN_ffff000090795c14
              (0,*(undefined4 *)(param_1 + 0xa8),*(undefined8 *)param_3,param_3[2],(char)param_3[3])
    ;
    uVar1 = 0;
    break;
  case 0x102:
    uVar1 = 0;
    *param_3 = 2;
    break;
  case 0x103:
    uVar1 = FUN_ffff000090794764(param_3);
    return uVar1;
  }
  return uVar1;
}


