// File offset entry: 0xa4bc

/* WARNING: Possible PIC construction at 0x0200a418: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0200a440: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0200a41c) */
/* WARNING: Removing unreachable block (ram,0x0200a420) */
/* WARNING: Removing unreachable block (ram,0x0200a434) */
/* WARNING: Removing unreachable block (ram,0x0200a444) */
/* WARNING: Removing unreachable block (ram,0x0200a448) */
/* WARNING: Removing unreachable block (ram,0x0200a458) */
/* WARNING: Removing unreachable block (ram,0x0200a468) */
/* WARNING: Removing unreachable block (ram,0x0200a53c) */
/* WARNING: Removing unreachable block (ram,0x0200a478) */
/* WARNING: Removing unreachable block (ram,0x0200a494) */
/* WARNING: Removing unreachable block (ram,0x0200a4bc) */
/* WARNING: Removing unreachable block (ram,0x0200a4f0) */
/* WARNING: Removing unreachable block (ram,0x0200a4fc) */
/* WARNING: Removing unreachable block (ram,0x0200a560) */

undefined8 FUN_0200a3bc(undefined8 param_1,undefined8 param_2,ulong param_3)

{
  char cVar1;
  undefined8 uVar2;
  long lVar3;
  undefined8 in_x6;
  undefined8 in_x7;
  undefined8 uVar4;
  undefined1 auVar5 [16];
  long lStack_50;
  long lStack_48;
  
  lStack_48 = *plRam0000000002103280;
  uVar2 = func_0x02091544(param_1,0x20c8c6c);
  if ((int)uVar2 == 0) {
    auVar5._8_8_ = 0x20bb54f;
    auVar5._0_8_ = &lStack_50;
    lStack_50 = 0;
    uVar4 = 0x200a41c;
  }
  else {
    if (*plRam0000000002103280 == lStack_48) {
      return uVar2;
    }
    param_3 = func_0x02091090();
    param_3 = param_3 & 0xffffffff;
    cVar1 = func_0x020a2d70();
    uVar2 = 0x20cbf60;
    if (cVar1 != '\0') {
      uVar2 = 0x20cf237;
    }
    FUN_0208ada0(0x20c47f4,0x20b8356,uVar2,param_3);
    uVar2 = func_0x0200a6d0();
    uVar4 = 0x200a5cc;
    auVar5 = func_0x0208b224(uVar2,0xbb);
  }
  lVar3 = func_0x0206b7f0(0x10000,0x10000,0xffffffffffffffff,0,0,auVar5._8_8_,in_x6,in_x7,
                          &stack0xffffffffffffffc0,uVar4,param_3);
  if (lVar3 == 0) {
    uVar2 = 0x10a008;
  }
  else {
    uVar2 = 0;
    *auVar5._0_8_ = lVar3;
  }
  return uVar2;
}


