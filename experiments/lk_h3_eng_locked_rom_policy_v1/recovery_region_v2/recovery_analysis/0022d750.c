
/* std::string android::base::Join<std::vector<std::string, std::allocator<std::string > >, char
   const*>(std::vector<std::string, std::allocator<std::string > > const&, char const*) */

void __thiscall
android::base::Join<std::vector<std::string,std::allocator<std::string>>,char_const*>
          (base *this,vector *param_1,char *param_2)

{
  undefined *puVar1;
  ulong uVar2;
  byte *pbVar3;
  byte *pbVar4;
  long lVar5;
  undefined *puVar6;
  undefined *puVar7;
  size_t sVar8;
  ostream *poVar9;
  undefined8 uVar10;
  ulong *in_x8;
  long lVar11;
  byte *pbVar12;
  undefined8 *puVar13;
  undefined1 *__dest;
  ulong uVar14;
  void *__src;
  undefined *local_170;
  undefined *local_168;
  undefined8 uStack_160;
  undefined8 local_158;
  undefined8 uStack_150;
  ulong local_148;
  undefined8 uStack_140;
  ulong local_138;
  undefined8 uStack_130;
  ulong local_128;
  undefined8 uStack_120;
  void *local_118;
  ulong uStack_110;
  undefined8 local_108;
  undefined *puStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  long local_68;

  puVar7 = PTR_VTT_00429b88;
  puVar6 = PTR_vtable_00429b80;
  lVar5 = tpidr_el0;
  local_68 = *(long *)(lVar5 + 0x28);
  if (*(long *)this == *(long *)(this + 8)) {
    *(undefined2 *)in_x8 = 0;
  }
  else {
    local_108 = 0;
    uStack_70 = 0;
    local_78 = 0;
    uStack_80 = 0;
    local_88 = 0;
    uStack_90 = 0;
    local_98 = 0;
    uStack_a0 = 0;
    local_a8 = 0;
    uStack_b0 = 0;
    local_b8 = 0;
    uStack_c0 = 0;
    local_c8 = 0;
    uStack_d0 = 0;
    local_d8 = 0;
    uStack_e0 = 0;
    local_e8 = 0;
    uStack_f0 = 0;
    local_f8 = 0;
    uStack_110 = 0;
    local_118 = (void *)0x0;
    uStack_120 = 0;
    local_128 = 0;
    uStack_130 = 0;
    local_138 = 0;
    puVar1 = PTR_vtable_00429b80 + 0x40;
    uStack_140 = 0;
    local_148 = 0;
    local_170 = *(undefined **)(PTR_VTT_00429b88 + 8);
    uStack_150 = 0;
    local_158 = 0;
    uStack_160 = 0;
    local_168 = (undefined *)0x0;
    puStack_100 = puVar1;
    *(undefined8 *)((long)&local_170 + *(long *)(local_170 + -0x18)) =
         *(undefined8 *)(PTR_VTT_00429b88 + 0x10);
    lVar11 = *(long *)(local_170 + -0x18);
    std::ios_base::init((void *)((long)&local_170 + lVar11));
    local_170 = puVar6 + 0x18;
    *(undefined8 *)((long)&local_e8 + lVar11) = 0;
    *(undefined4 *)((long)&uStack_e0 + lVar11) = 0xffffffff;
    puStack_100 = puVar1;
    std::streambuf::streambuf((streambuf *)&local_168);
    puVar6 = PTR_vtable_00429b90;
    local_168 = PTR_vtable_00429b90 + 0x10;
    local_108 = CONCAT44(local_108._4_4_,0x10);
    pbVar12 = *(byte **)this;
    uStack_120 = 0;
    local_128 = 0;
    uStack_110 = 0;
    local_118 = (void *)0x0;
    uVar14 = *(ulong *)(pbVar12 + 8);
    pbVar3 = *(byte **)(pbVar12 + 0x10);
    if ((*pbVar12 & 1) == 0) {
      pbVar3 = pbVar12 + 1;
      uVar14 = (ulong)(*pbVar12 >> 1);
    }
    std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
              ((ostream *)&local_170,(char *)pbVar3,uVar14);
    pbVar3 = *(byte **)this;
    while (pbVar12 = pbVar3 + 0x18, pbVar12 != *(byte **)(this + 8)) {
      sVar8 = strlen((char *)param_1);
      poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                         ((ostream *)&local_170,(char *)param_1,sVar8);
      uVar14 = *(ulong *)(pbVar3 + 0x20);
      pbVar4 = *(byte **)(pbVar3 + 0x28);
      if ((*pbVar12 & 1) == 0) {
        pbVar4 = pbVar3 + 0x19;
        uVar14 = (ulong)(*pbVar12 >> 1);
      }
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar9,(char *)pbVar4,uVar14);
      pbVar3 = pbVar12;
    }
    if (((uint)local_108 >> 4 & 1) == 0) {
      if (((uint)local_108 >> 3 & 1) != 0) {
        puVar13 = &local_158;
        uVar14 = local_148;
        goto LAB_0022d928;
      }
      uVar14 = 0;
      __dest = (undefined1 *)((long)in_x8 + 1);
      *(undefined1 *)in_x8 = 0;
    }
    else {
      if (uStack_110 < local_138) {
        uStack_110 = local_138;
      }
      puVar13 = &uStack_140;
      uVar14 = uStack_110;
LAB_0022d928:
      __src = (void *)*puVar13;
      uVar14 = uVar14 - (long)__src;
      if (0xffffffffffffffef < uVar14) {
        if (*(long *)(lVar5 + 0x28) == local_68) {
          uVar10 = std::string::__throw_length_error_abi_ne180000_();
          std::ostringstream::~ostringstream((ostringstream *)&local_170);
          std::ios::~ios((ios *)&puStack_100);
          if (*(long *)(lVar5 + 0x28) == local_68) {
                    /* WARNING: Subroutine does not return */
            _Unwind_Resume(uVar10);
          }
        }
        goto LAB_0022da70;
      }
      if (uVar14 < 0x17) {
        __dest = (undefined1 *)((long)in_x8 + 1);
        *(char *)in_x8 = (char)((int)uVar14 << 1);
        if (uVar14 == 0) goto LAB_0022d980;
      }
      else {
        uVar2 = (uVar14 | 0xf) + 1;
        __dest = operator_new(uVar2);
        in_x8[1] = uVar14;
        in_x8[2] = (ulong)__dest;
        *in_x8 = uVar2 | 1;
      }
      memmove(__dest,__src,uVar14);
    }
LAB_0022d980:
    local_170 = *(undefined **)puVar7;
    __dest[uVar14] = 0;
    *(undefined8 *)((long)&local_170 + *(long *)(local_170 + -0x18)) =
         *(undefined8 *)(puVar7 + 0x18);
    local_168 = puVar6 + 0x10;
    if ((local_128 & 1) != 0) {
      operator_delete(local_118);
    }
    std::streambuf::~streambuf((streambuf *)&local_168);
    std::ostream::~ostream((ostream *)&local_170);
    std::ios::~ios((ios *)&puStack_100);
  }
  if (*(long *)(lVar5 + 0x28) == local_68) {
    return;
  }
LAB_0022da70:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
