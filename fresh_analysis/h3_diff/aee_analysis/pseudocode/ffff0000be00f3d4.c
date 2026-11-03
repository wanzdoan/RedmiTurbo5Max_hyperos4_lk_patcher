
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be00f3d4(uint param_1,uint param_2,undefined8 param_3)

{
  byte bVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  uint *puVar5;
  ulong uVar6;
  
  uVar6 = 0;
  puVar5 = (uint *)0xffff0000be0fb038;
  do {
    if (puVar5[1] == 0) {
      lVar3 = FUN_ffff0000be063614();
      lVar3 = lVar3 - (ulong)param_2;
      puVar5[3] = param_2;
      puVar5[4] = param_1;
      bVar1 = DAT_ffff0000be0fd064;
      puVar5[2] = (uint)lVar3;
      if ((bVar1 & 1) == 0) {
        lVar4 = FUN_ffff0000be0610a8(param_3,&DAT_ffff0000be0c0377);
        *puVar5 = (uint)(lVar4 != 0);
        FUN_ffff0000be060df0(puVar5 + 8,param_3,0x20);
      }
      else {
        *puVar5 = 1;
        iVar2 = FUN_ffff0000be0611dc(puVar5 + 8,0x20,s__s_gz_ffff0000be0bab1d,param_3);
        if (iVar2 < 0) {
          FUN_ffff0000be063b40
                    (s__s_snprintf_failed_ffff0000be0c2947,s_kedump_add2hdr_ffff0000be0a69a9);
          FUN_ffff0000be05fd98
                    (s__s_snprintf_failed_ffff0000be0c2947,s_kedump_add2hdr_ffff0000be0a69a9);
        }
      }
      puVar5[1] = 1;
      DAT_ffff0000be0fd064 = 0;
      FUN_ffff0000be063b40
                (s_kedump_add___s__d___x__x__llx_ffff0000be0a3121,puVar5 + 8,uVar6 & 0xffffffff,
                 param_2,param_1,lVar3);
      FUN_ffff0000be05fd98
                (s_kedump_add___s__d___x__x__llx_ffff0000be0a3121,puVar5 + 8,uVar6 & 0xffffffff,
                 (ulong)param_2,param_1,lVar3);
      goto LAB_ffff0000be00f51c;
    }
    uVar6 = uVar6 + 1;
    puVar5 = puVar5 + 0x10;
  } while (uVar6 != 0x80);
  FUN_ffff0000be063614();
  puVar5 = (uint *)&DAT_ffff0000be0fcff8;
LAB_ffff0000be00f51c:
  uVar6 = FUN_ffff0000be062f38();
  if ((uVar6 & 1) == 0) {
    FUN_ffff0000be063628(_DAT_ffff0000be0fd058,puVar5,0x40);
  }
  _DAT_ffff0000be0fd058 = _DAT_ffff0000be0fd058 + 0x40;
  return;
}


