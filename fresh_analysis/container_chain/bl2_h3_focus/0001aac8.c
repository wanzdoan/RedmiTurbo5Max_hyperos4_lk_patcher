// File offset entry: 0x1aac8

undefined4 FUN_ffff0000b801aac8(long param_1,uint param_2)

{
  byte bVar1;
  undefined4 *puVar2;
  undefined8 uVar3;
  int iVar4;
  ulong uVar5;
  int iVar6;
  undefined4 local_24;
  
  if (*(int *)(param_1 + 8) <= (int)param_2) {
    return 0xffffffff;
  }
  uVar5 = (ulong)param_2;
  do {
    iVar4 = (int)uVar5;
    uVar5 = (long)iVar4 + 1;
    bVar1 = *(byte *)(*(long *)(param_1 + 0x18) + (long)iVar4);
    if (bVar1 < 0x78) {
      if ((bVar1 == 0 || bVar1 == 0x1c) || (bVar1 == 0x5c)) goto LAB_ffff0000b801ab68;
LAB_ffff0000b801ab04:
      puVar2 = (undefined4 *)0x0;
    }
    else {
      if (0xdb < bVar1) {
        if (bVar1 == 0xdc) goto LAB_ffff0000b801ab68;
        if (bVar1 == 0xf0) {
          uVar5 = (ulong)DAT_ffff0000b80e7ed0;
          DAT_ffff0000b80e7ed0 = DAT_ffff0000b80e7ed0 - 1;
          return (&DAT_ffff0000b80e7d40)[uVar5];
        }
        goto LAB_ffff0000b801ab04;
      }
      if ((bVar1 != 0x78) && (bVar1 != 0x9c)) goto LAB_ffff0000b801ab04;
LAB_ffff0000b801ab68:
      local_24 = 0;
      if ((bVar1 & 0xbf) == 0x1c) {
        iVar6 = 2;
        uVar3 = 1;
LAB_ffff0000b801abac:
        FUN_ffff0000b804c8b4(&local_24,*(long *)(param_1 + 0x18) + uVar5,uVar3);
        uVar5 = (ulong)(uint)(iVar4 + iVar6);
      }
      else {
        if ((bVar1 & 0xbf) == 0x9c) {
          iVar6 = 3;
          uVar3 = 2;
          goto LAB_ffff0000b801abac;
        }
        iVar6 = 5;
        uVar3 = 4;
        if ((bVar1 == 0x78) || (bVar1 == 0)) goto LAB_ffff0000b801abac;
      }
      puVar2 = &local_24;
    }
    FUN_ffff0000b801ac00(bVar1,puVar2);
    uVar5 = uVar5 & 0xffffffff;
  } while( true );
}


