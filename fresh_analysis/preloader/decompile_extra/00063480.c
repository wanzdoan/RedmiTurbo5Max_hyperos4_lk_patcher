// File offset entry: 0x63480

/* WARNING: Possible PIC construction at 0x020633f0: Changing call to branch */
/* WARNING: Possible PIC construction at 0x02063480: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020634e8: Changing call to branch */
/* WARNING: Possible PIC construction at 0x02063580: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020635fc: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0206379c: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020638f8: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0206384c: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020638fc) */
/* WARNING: Removing unreachable block (ram,0x02063918) */
/* WARNING: Removing unreachable block (ram,0x02063900) */
/* WARNING: Removing unreachable block (ram,0x020637a0) */
/* WARNING: Removing unreachable block (ram,0x020637b8) */
/* WARNING: Removing unreachable block (ram,0x0206380c) */
/* WARNING: Removing unreachable block (ram,0x02063820) */
/* WARNING: Removing unreachable block (ram,0x020638f4) */
/* WARNING: Removing unreachable block (ram,0x02063830) */
/* WARNING: Removing unreachable block (ram,0x02063870) */
/* WARNING: Removing unreachable block (ram,0x0206387c) */
/* WARNING: Removing unreachable block (ram,0x02063834) */
/* WARNING: Removing unreachable block (ram,0x02063884) */
/* WARNING: Removing unreachable block (ram,0x020638c8) */
/* WARNING: Removing unreachable block (ram,0x02063890) */
/* WARNING: Removing unreachable block (ram,0x02063898) */
/* WARNING: Removing unreachable block (ram,0x0206383c) */
/* WARNING: Removing unreachable block (ram,0x02063844) */
/* WARNING: Removing unreachable block (ram,0x02063800) */
/* WARNING: Removing unreachable block (ram,0x020637a4) */
/* WARNING: Removing unreachable block (ram,0x02063600) */
/* WARNING: Removing unreachable block (ram,0x02063614) */
/* WARNING: Removing unreachable block (ram,0x0206361c) */
/* WARNING: Removing unreachable block (ram,0x02063660) */
/* WARNING: Removing unreachable block (ram,0x02063678) */
/* WARNING: Removing unreachable block (ram,0x02063684) */
/* WARNING: Removing unreachable block (ram,0x0206368c) */
/* WARNING: Removing unreachable block (ram,0x02063724) */
/* WARNING: Removing unreachable block (ram,0x02063730) */
/* WARNING: Removing unreachable block (ram,0x02063708) */
/* WARNING: Removing unreachable block (ram,0x02063714) */
/* WARNING: Removing unreachable block (ram,0x020636ec) */
/* WARNING: Removing unreachable block (ram,0x020636dc) */
/* WARNING: Removing unreachable block (ram,0x0206373c) */
/* WARNING: Removing unreachable block (ram,0x020636e4) */
/* WARNING: Removing unreachable block (ram,0x020636f8) */
/* WARNING: Removing unreachable block (ram,0x020636ac) */
/* WARNING: Removing unreachable block (ram,0x02063744) */
/* WARNING: Removing unreachable block (ram,0x0206374c) */
/* WARNING: Removing unreachable block (ram,0x020636bc) */
/* WARNING: Removing unreachable block (ram,0x0206376c) */
/* WARNING: Removing unreachable block (ram,0x02063654) */
/* WARNING: Removing unreachable block (ram,0x0206379c) */
/* WARNING: Removing unreachable block (ram,0x02063604) */
/* WARNING: Removing unreachable block (ram,0x02063584) */
/* WARNING: Removing unreachable block (ram,0x0206359c) */
/* WARNING: Removing unreachable block (ram,0x020635dc) */
/* WARNING: Removing unreachable block (ram,0x020635d4) */
/* WARNING: Removing unreachable block (ram,0x020635fc) */
/* WARNING: Removing unreachable block (ram,0x02063588) */
/* WARNING: Removing unreachable block (ram,0x020634ec) */
/* WARNING: Removing unreachable block (ram,0x02063500) */
/* WARNING: Removing unreachable block (ram,0x02063544) */
/* WARNING: Removing unreachable block (ram,0x0206354c) */
/* WARNING: Removing unreachable block (ram,0x02063580) */
/* WARNING: Removing unreachable block (ram,0x020634f0) */
/* WARNING: Removing unreachable block (ram,0x02063484) */
/* WARNING: Removing unreachable block (ram,0x0206349c) */
/* WARNING: Removing unreachable block (ram,0x020634dc) */
/* WARNING: Removing unreachable block (ram,0x020634d4) */
/* WARNING: Removing unreachable block (ram,0x020634e8) */
/* WARNING: Removing unreachable block (ram,0x02063488) */
/* WARNING: Removing unreachable block (ram,0x020633f4) */
/* WARNING: Removing unreachable block (ram,0x0206340c) */
/* WARNING: Removing unreachable block (ram,0x02063450) */
/* WARNING: Removing unreachable block (ram,0x02063458) */
/* WARNING: Removing unreachable block (ram,0x02063478) */
/* WARNING: Removing unreachable block (ram,0x0206347c) */
/* WARNING: Removing unreachable block (ram,0x02063480) */
/* WARNING: Removing unreachable block (ram,0x020633f8) */
/* WARNING: Removing unreachable block (ram,0x02063850) */
/* WARNING: Removing unreachable block (ram,0x02063864) */
/* WARNING: Removing unreachable block (ram,0x020638d0) */
/* WARNING: Removing unreachable block (ram,0x020638d4) */
/* WARNING: Removing unreachable block (ram,0x020638f8) */
/* WARNING: Recovered jumptable eliminated as dead code */

undefined8 FUN_02063380(undefined4 param_1,uint param_2)

{
  bool bVar1;
  bool bVar2;
  undefined8 uVar3;
  ulong extraout_x8;
  uint uVar4;
  undefined4 uStack_2c;
  undefined8 uStack_28;
  
  uStack_28 = *puRam0000000002103280;
  uStack_2c = param_1;
  func_0x0206333c(&uStack_2c);
  bVar1 = param_2 != 0;
  bVar2 = param_2 == 1;
  uVar3 = 0xfffffffe;
  if ((param_2 < 2) && (uVar3 = func_0x0206393c(0xfffffffe), !bVar1 || bVar2)) {
    uVar4 = 0x1002d004;
    uVar3 = 0;
    if (param_2 == 0) {
      uVar4 = 0x1002d008;
    }
    *(int *)((extraout_x8 >> 1 & 0x7ffffff0) + (ulong)uVar4) =
         1 << (ulong)((uint)extraout_x8 & 0x1f);
  }
  return uVar3;
}


