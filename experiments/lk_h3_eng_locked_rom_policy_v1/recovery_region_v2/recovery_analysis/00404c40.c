
/* WARNING: Removing unreachable block (ram,0x00404cdc) */
/* StringVectorToNullTerminatedArray(std::vector<std::string, std::allocator<std::string > > const&)
    */

void StringVectorToNullTerminatedArray(vector *param_1)

{
  ulong uVar1;
  byte *pbVar2;
  long *__s;
  void *pvVar3;
  undefined8 *in_x8;
  long lVar4;
  long *plVar5;
  byte *pbVar6;
  ulong uVar7;
  long *plVar8;
  long *plVar9;
  byte *pbVar10;

  pbVar10 = *(byte **)param_1;
  pbVar2 = *(byte **)(param_1 + 8);
  in_x8[1] = 0;
  in_x8[2] = 0;
  lVar4 = (long)pbVar2 - (long)pbVar10;
  *in_x8 = 0;
  if (lVar4 == 0) {
    plVar8 = (long *)0x0;
    __s = (long *)0x0;
  }
  else {
    lVar4 = lVar4 >> 3;
    if ((ulong)(lVar4 * -0x5555555555555555) >> 0x3d != 0) goto LAB_00404da0;
    __s = operator_new(lVar4 * 0x5555555555555558);
    plVar8 = __s + lVar4 * 0xaaaaaaaaaaaaaab;
    *in_x8 = __s;
    in_x8[2] = plVar8;
    memset(__s,0,lVar4 * 0x5555555555555558);
    in_x8[1] = plVar8;
    plVar5 = __s;
    do {
      pbVar6 = *(byte **)(pbVar10 + 0x10);
      if ((*pbVar10 & 1) == 0) {
        pbVar6 = pbVar10 + 1;
      }
      pbVar10 = pbVar10 + 0x18;
      *plVar5 = (long)pbVar6;
      plVar5 = plVar5 + 1;
    } while (pbVar10 != pbVar2);
  }
  lVar4 = (long)plVar8 - (long)__s >> 3;
  uVar1 = lVar4 + 1;
  if (uVar1 >> 0x3d == 0) {
    uVar7 = (long)plVar8 - (long)__s >> 2;
    if (uVar7 <= uVar1) {
      uVar7 = uVar1;
    }
    if (0x7ffffffffffffff7 < (ulong)((long)plVar8 - (long)__s)) {
      uVar7 = 0x1fffffffffffffff;
    }
    if (uVar7 == 0) {
      pvVar3 = (void *)0x0;
    }
    else {
      if (uVar7 >> 0x3d != 0) {
                    /* WARNING: Subroutine does not return */
        std::__throw_bad_array_new_length_abi_nn180000_();
      }
      pvVar3 = operator_new(uVar7 << 3);
    }
    plVar5 = (long *)((long)pvVar3 + lVar4 * 8);
    plVar9 = plVar5 + 1;
    *plVar5 = 0;
    if (plVar8 != __s) {
      do {
        plVar8 = plVar8 + -1;
        plVar5 = plVar5 + -1;
        *plVar5 = *plVar8;
      } while (plVar8 != __s);
      plVar8 = (long *)*in_x8;
    }
    *in_x8 = plVar5;
    in_x8[1] = plVar9;
    in_x8[2] = (void *)((long)pvVar3 + uVar7 * 8);
    if (plVar8 != (long *)0x0) {
      operator_delete(plVar8);
    }
    in_x8[1] = plVar9;
    return;
  }
LAB_00404da0:
                    /* WARNING: Subroutine does not return */
  std::vector<unsigned_long,std::allocator<unsigned_long>>::__throw_length_error_abi_nn180000_();
}
