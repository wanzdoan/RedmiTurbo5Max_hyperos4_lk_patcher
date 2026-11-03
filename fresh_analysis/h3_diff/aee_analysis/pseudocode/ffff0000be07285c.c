
/* WARNING: Removing unreachable block (ram,0xffff0000be072178) */

ulong FUN_ffff0000be07285c(long param_1,undefined4 param_2,int *param_3)

{
  uint uVar1;
  long lVar2;
  ulong uVar3;
  
  switch(param_2) {
  case 0x100:
    if ((param_3 != (int *)0x0) && (lVar2 = FUN_ffff0000be070acc(0), *param_3 - 1U < 2)) {
      uVar3 = (**(code **)(lVar2 + 0x220))(lVar2,4,0,0,0,&stack0xffffffffffffffec);
      return uVar3;
    }
  default:
    uVar3 = 0xffffffe8;
    break;
  case 0x101:
    FUN_ffff0000be05fd98
              (s__UFS__d__lu__u__u_ffff0000be0c37dc,*(undefined4 *)(param_1 + 0xa8),
               *(undefined8 *)param_3,param_3[2],(char)param_3[3]);
    FUN_ffff0000be073388
              (0,*(undefined4 *)(param_1 + 0xa8),*(undefined8 *)param_3,param_3[2],(char)param_3[3])
    ;
    uVar3 = 0;
    break;
  case 0x102:
    uVar3 = 0;
    *param_3 = 2;
    break;
  case 0x103:
    lVar2 = FUN_ffff0000be070acc(0);
    if (param_3 != (int *)0x0) {
      FUN_ffff0000be01a730(lVar2 + 0x270,0xffffffff);
      uVar1 = (**(code **)(lVar2 + 0x220))(lVar2,3,0,0,0,&stack0xffffffffffffffec);
      FUN_ffff0000be01a7c8(lVar2 + 0x270);
      if (uVar1 != 0) {
        FUN_ffff0000be05fd98(s__UFS__err__read_ATTR_B_BOOT_LUN__ffff0000be0a8937,uVar1);
        return (ulong)uVar1;
      }
      FUN_ffff0000be05fd98(s__UFS__err__invalid_ATTR_B_BOOT_L_ffff0000be0bdb0a,*param_3);
    }
    return 0xffffffff;
  }
  return uVar3;
}


