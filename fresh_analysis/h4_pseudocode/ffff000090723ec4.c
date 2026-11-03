
void FUN_ffff000090723ec4(void)

{
  long lVar1;
  byte bVar2;
  byte bVar3;
  byte bVar4;
  byte bVar5;
  long lVar6;
  ulong uVar7;
  undefined8 *puVar8;
  
  uVar7 = (ulong)DAT_ffff000090860920;
  if ((int)DAT_ffff000090860920 < 1) {
    DAT_ffff000090861468 = 0;
    DAT_ffff00009086146c = 0;
  }
  else {
    DAT_ffff000090861468 = 0;
    DAT_ffff00009086146c = 0;
    puVar8 = &DAT_ffff0000908607b8;
    lVar6 = 0;
    do {
      bVar2 = *(byte *)((long)puVar8 + 0x14);
      bVar3 = *(byte *)((long)puVar8 + 0x15);
      DAT_ffff000090861468 = DAT_ffff000090861468 + 8;
      bVar4 = *(byte *)((long)puVar8 + 0x16);
      lVar1 = lVar6 + 0xc0;
      bVar5 = *(byte *)((long)puVar8 + 0x17);
      *(uint *)((long)&DAT_ffff000090860938 + lVar6) = (uint)bVar2;
      *(uint *)((long)&DAT_ffff000090860950 + lVar6) = (uint)bVar3;
      *(uint *)((long)&DAT_ffff000090860998 + lVar6) = (uint)bVar2;
      *(uint *)((long)&DAT_ffff0000908609b0 + lVar6) = (uint)bVar3;
      *(undefined8 **)((long)&DAT_ffff000090860928 + lVar6) = puVar8;
      *(undefined8 **)((long)&DAT_ffff000090860940 + lVar6) = puVar8;
      *(undefined8 **)((long)&DAT_ffff000090860958 + lVar6) = puVar8;
      DAT_ffff00009086146c =
           DAT_ffff00009086146c + ((uint)bVar3 + (uint)bVar2 + (uint)bVar4 + (uint)bVar5) * 2;
      *(undefined8 **)((long)&DAT_ffff000090860970 + lVar6) = puVar8;
      *(undefined8 **)((long)&DAT_ffff000090860988 + lVar6) = puVar8;
      *(undefined8 **)((long)&DAT_ffff0000908609a0 + lVar6) = puVar8;
      *(undefined8 **)((long)&DAT_ffff0000908609b8 + lVar6) = puVar8;
      *(undefined8 **)((long)&DAT_ffff0000908609d0 + lVar6) = puVar8;
      puVar8 = puVar8 + 3;
      (&DAT_ffff000090860934)[lVar6] = 0;
      *(undefined4 *)((long)&DAT_ffff000090860930 + lVar6) = 0;
      (&DAT_ffff00009086094c)[lVar6] = 0;
      *(undefined4 *)((long)&DAT_ffff000090860948 + lVar6) = 1;
      *(undefined4 *)((long)&DAT_ffff000090860960 + lVar6) = 2;
      (&DAT_ffff000090860964)[lVar6] = 0;
      *(uint *)((long)&DAT_ffff000090860968 + lVar6) = (uint)bVar4;
      (&DAT_ffff00009086097c)[lVar6] = 0;
      *(uint *)((long)&DAT_ffff000090860980 + lVar6) = (uint)bVar5;
      *(undefined4 *)((long)&DAT_ffff000090860978 + lVar6) = 3;
      (&DAT_ffff000090860994)[lVar6] = 1;
      *(undefined4 *)((long)&DAT_ffff000090860990 + lVar6) = 0;
      (&DAT_ffff0000908609ac)[lVar6] = 1;
      *(undefined4 *)((long)&DAT_ffff0000908609a8 + lVar6) = 1;
      *(undefined4 *)((long)&DAT_ffff0000908609c0 + lVar6) = 2;
      (&DAT_ffff0000908609c4)[lVar6] = 1;
      *(uint *)((long)&DAT_ffff0000908609c8 + lVar6) = (uint)bVar4;
      (&DAT_ffff0000908609dc)[lVar6] = 1;
      *(uint *)((long)&DAT_ffff0000908609e0 + lVar6) = (uint)bVar5;
      *(undefined4 *)((long)&DAT_ffff0000908609d8 + lVar6) = 3;
      lVar6 = lVar1;
    } while (uVar7 * 0xc0 - lVar1 != 0);
  }
  return;
}


